package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.SuppliersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SupplierDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SupplierDAO>(SuppliersTable)

    var inn by SuppliersTable.inn
    var name by SuppliersTable.name
    var requisites by SuppliersTable.requisites
    var email by SuppliersTable.email
    var phone by SuppliersTable.phone
    var address by SuppliersTable.address
}