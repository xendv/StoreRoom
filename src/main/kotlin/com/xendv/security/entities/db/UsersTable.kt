package com.xendv.security.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable("users") {
    val login = varchar("login", length = 256)
    val password = varchar("password", length = 256)
}