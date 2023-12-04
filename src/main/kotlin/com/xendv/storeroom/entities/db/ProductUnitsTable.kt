package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object ProductUnitsTable : IdTable<String>("product_unit") {
    override val id: Column<EntityID<String>> = ProductsTable.varchar("id", 256).entityId()

    val sku = reference("sku", ProductsTable.id)
    val lot = reference("lot", LotsTable.id)

    override val primaryKey = PrimaryKey(ProductsTable.id)
}