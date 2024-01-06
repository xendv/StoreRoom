package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object LotsTable : IntIdTable("lot") {
    val sku = reference("sku", ProductsTable.id)
    val supplier = reference("supplier", SuppliersTable.id)
    val supplyDate = datetime("supplyDate")
    val expirationDate = datetime("expirationDate").nullable()
}