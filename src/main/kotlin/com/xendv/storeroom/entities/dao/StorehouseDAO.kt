package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.StorehouseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StorehouseDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StorehouseDAO>(StorehouseTable)

    var name by StorehouseTable.name
    var address by StorehouseTable.address
}