package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ExpensesTable : IntIdTable("expense") {
    val units = varchar("units", length = 256).clientDefault { listOf<String>().toString() }
    val date = datetime("date")
}