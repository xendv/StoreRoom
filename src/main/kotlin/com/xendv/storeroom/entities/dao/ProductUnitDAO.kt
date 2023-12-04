package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.ProductUnitsTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductUnitDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ProductUnitDAO>(ProductUnitsTable)

    var sku by ProductDAO referencedOn ProductUnitsTable.id
    var lot by LotDAO referencedOn ProductUnitsTable.lot
}