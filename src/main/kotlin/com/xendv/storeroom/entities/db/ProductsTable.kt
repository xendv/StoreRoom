package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object ProductsTable : IdTable<String>("product") {
    override val id: Column<EntityID<String>> = varchar("sku", 256).entityId()

    val barcode = varchar("barcode", length = 256)
    val name = varchar("name", length = 256)
    val description = varchar("description", length = 256).nullable()
    val measurement = varchar("measurement", length = 256).nullable()

    override val primaryKey = PrimaryKey(id)
}