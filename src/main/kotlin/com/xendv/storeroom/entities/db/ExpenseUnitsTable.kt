package com.xendv.storeroom.entities.db

import org.jetbrains.exposed.dao.id.IntIdTable

object ExpenseUnitsTable : IntIdTable("expense_unit") {
    val expense = reference("expense", ExpensesTable.id)
    val unit = reference("unit", ProductUnitsTable.id)
}