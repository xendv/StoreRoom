package com.xendv.security.entities.dao

import com.xendv.security.entities.db.RolesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoleDAO>(RolesTable)

    var role by RolesTable.name
}