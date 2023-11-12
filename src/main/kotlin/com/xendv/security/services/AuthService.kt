package com.xendv.security.services

import com.xendv.security.entities.Credentials
import com.xendv.security.entities.UserEntity
import com.xendv.security.entities.UserRole
import com.xendv.security.entities.dao.RoleDAO
import com.xendv.security.entities.dao.UserDAO
import com.xendv.security.entities.dao.UserRolesDAO
import com.xendv.security.entities.db.RolesTable
import com.xendv.security.entities.db.UserRolesTable
import com.xendv.security.entities.db.UsersTable
import com.xendv.security.entities.requests.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val database: Database,
    private val jwtService: JwtService,
) {

    init {
        transaction(database) {
            SchemaUtils.create(UsersTable, UserRolesTable, RolesTable)
        }
    }

    suspend fun getAllUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        transaction(database) {
            UserDAO.all().map { it.toEntity() }
        }
    }

    suspend fun getUser(login: String): UserEntity = withContext(Dispatchers.IO) {
        transaction(database) {
            UserDAO.find { UsersTable.login eq login }.first().toEntity()
        }
    }

    suspend fun getUser(id: Int): UserEntity = withContext(Dispatchers.IO) {
        transaction(database) {
            UserDAO[id].toEntity()
        }
    }

    suspend fun updateUser(
        user: UserEntity
    ) {
        // TODO: Create update function
    }

    suspend fun createUser(
        user: UserEntity,
        register: Boolean = false,
    ): UserEntity = withContext(Dispatchers.IO) {
        val login = requireNotNull(user.login) { "User login is not set!" }
        val password = requireNotNull(user.password) { "User password is not set!" }

        transaction(database) {
            require(
                !UserDAO.find { UsersTable.login eq login }.any()
            ) { "User with login $login already defined" }

            val createdUserDAO = UserDAO.new {
                this.login = login
                this.password = encrypt(password)
            }

            if (!register) {
                user.roles?.forEach { createdUserDAO.addRole(it) }
            } else {
                createdUserDAO.addRole(UserRole.USER)
            }

            createdUserDAO.toEntity()
        }
    }

    suspend fun performLogin(
        loginRequest: LoginRequest,
    ): Credentials = withContext(Dispatchers.IO) {
        val login = requireNotNull(loginRequest.login) {
            "Required field 'login' is empty!"
        }
        val password = requireNotNull(loginRequest.password) {
            "Required field 'password' is empty!"
        }

        transaction(database) {
            val user = UserDAO.find { UsersTable.login eq login }.first()
            require(
                checkPassword(
                    password = password,
                    hash = user.password,
                )
            )
            Credentials(
                login = login,
                token = jwtService.createToken(user),
            )
        }
    }

    private fun UserDAO.addRole(role: UserRole) {
        require(
            !roles.any { it.role.role == role }
        ) { "Role $role already added for user with login $login!" }
        UserRolesDAO.new {
            user = this@addRole
            this.role = RoleDAO.find(RolesTable.name eq role).first()
        }
    }

    private fun UserDAO.toEntity() = UserEntity(
        id = id.value,
        login = login,
        roles = roles.map { it.role.role }.toSet(),
    )

    private fun encrypt(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    private fun checkPassword(password: String, hash: String) = BCrypt.checkpw(password, hash)
}