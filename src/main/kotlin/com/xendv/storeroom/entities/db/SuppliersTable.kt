package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object SuppliersTable : IntIdTable("supplier") {
    val inn = varchar("inn", length = 256)
    val name = varchar("name", length = 256)
    val requisites = varchar("requisites", length = 256)
    val email = varchar("email", length = 256)
    val phone = varchar("phone", length = 256)
    val address = varchar("address", length = 256)
}