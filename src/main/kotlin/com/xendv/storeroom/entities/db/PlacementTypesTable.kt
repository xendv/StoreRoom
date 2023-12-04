package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object PlacementTypesTable : IntIdTable("placement") {
    val name = varchar("name", length = 256)
}