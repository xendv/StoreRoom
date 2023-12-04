package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object StorehouseTable : IntIdTable("storehouse") {
    val name = varchar("name", length = 256)
    val address = varchar("address", length = 256)
}