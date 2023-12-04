package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductInventoriesTable : IntIdTable("product_inventory") {
    val unit = reference("unit", ProductUnitsTable.id)
    val placement = varchar("placement", length = 256)
    val placementType = reference("id", PlacementTypesTable.id)
    val status = varchar("status", length = 256)
}