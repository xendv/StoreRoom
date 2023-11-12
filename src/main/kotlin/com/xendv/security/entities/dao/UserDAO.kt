package com.xendv.security.entities.dao

import com.xendv.security.entities.db.UserRolesTable
import com.xendv.security.entities.db.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UsersTable)

    var login by UsersTable.login
    var password by UsersTable.password
    val roles by UserRolesDAO referrersOn UserRolesTable.user
}