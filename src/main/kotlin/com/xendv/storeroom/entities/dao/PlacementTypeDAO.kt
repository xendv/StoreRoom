package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.PlacementTypesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlacementTypeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlacementTypeDAO>(PlacementTypesTable)

    var name by PlacementTypesTable.name
}