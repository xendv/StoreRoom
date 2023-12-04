package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.LotsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LotDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LotDAO>(LotsTable)

    var sku by ProductDAO referencedOn LotsTable.sku
    var supplier by SupplierDAO referencedOn LotsTable.supplier
    var supplyDate by LotsTable.supplyDate
    var expirationDate by LotsTable.expirationDate
}