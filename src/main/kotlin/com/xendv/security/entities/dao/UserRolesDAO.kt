package com.xendv.security.entities.dao

import com.xendv.security.entities.db.UserRolesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserRolesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRolesDAO>(UserRolesTable)

    var user by UserDAO referencedOn UserRolesTable.user
    var role by RoleDAO referencedOn UserRolesTable.role
}