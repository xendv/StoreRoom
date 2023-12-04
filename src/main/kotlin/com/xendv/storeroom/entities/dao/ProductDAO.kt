package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.ProductsTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ProductDAO>(ProductsTable)

    var barcode by ProductsTable.barcode
    var name by ProductsTable.name
    var description by ProductsTable.description
    var measurement by ProductsTable.measurement
}