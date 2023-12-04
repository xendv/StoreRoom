package com.xendv.storeroom.entities.dao

import com.xendv.storeroom.entities.db.ExpenseUnitsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ExpenseUnitDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExpenseUnitDAO>(ExpenseUnitsTable)

    var unit by ProductUnitDAO referencedOn ExpenseUnitsTable.unit
    var expense by ExpenseDAO referencedOn ExpenseUnitsTable.expense
}