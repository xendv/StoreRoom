package com.xendv.security.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object UserRolesTable : IntIdTable("user_roles") {
    val user = reference("user", UsersTable.id)
    val role = reference("role", RolesTable.id)
}