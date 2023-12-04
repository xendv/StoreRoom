package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.ProductInventoriesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductInventoryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductInventoryDAO>(ProductInventoriesTable)

    var unit by ProductUnitDAO referencedOn ProductInventoriesTable.unit
    var placement by ProductInventoriesTable.placement
    var placementType by PlacementTypeDAO referencedOn ProductInventoriesTable.placementType
    var status by ProductInventoriesTable.status
}