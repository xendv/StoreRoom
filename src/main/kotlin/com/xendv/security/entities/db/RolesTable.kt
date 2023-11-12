package com.xendv.security.entities.db

import com.xendv.security.entities.UserRole
import org.jetbrains.exposed.dao.id.IntIdTable

object RolesTable : IntIdTable("roles") {
    val name = enumerationByName<UserRole>("name", 32)
}