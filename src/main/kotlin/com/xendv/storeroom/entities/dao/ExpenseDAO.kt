package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.ExpenseUnitsTable
import com.xendv.storeroom.entities.db.ExpensesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ExpenseDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExpenseDAO>(ExpensesTable)

    var date by ExpensesTable.date
    var units by ProductUnitDAO via ExpenseUnitsTable
}