package com.xendv.storeroom.services

import com.xendv.storeroom.entities.ExpenseUnit
import com.xendv.storeroom.entities.dao.ExpenseDAO
import com.xendv.storeroom.entities.dao.ExpenseUnitDAO
import com.xendv.storeroom.entities.dao.ProductUnitDAO
import com.xendv.storeroom.entities.db.ExpenseUnitsTable
import com.xendv.storeroom.entities.db.ExpensesTable
import com.xendv.storeroom.entities.db.ProductUnitsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ExpenseUnitService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(ExpenseUnitsTable)
        }
    }

    fun associateExpenseWithUnits(expenseId: Int, unitIds: List<String>) {
        transaction {
            unitIds.forEach { unitId ->
                ExpenseUnitsTable.insertAndGetId {
                    it[expense] = EntityID(expenseId, ExpensesTable)
                    it[unit] = EntityID(unitId, ProductUnitsTable)
                }
            }
        }
    }

    fun getUnitsForExpense(expenseId: Int): List<ProductUnitDAO> {
        return transaction {
            ExpenseUnitsTable.select { ExpenseUnitsTable.expense eq EntityID(expenseId, ExpensesTable) }
                .map { it[ExpenseUnitsTable.unit] }.mapNotNull { ProductUnitDAO.findById(it.value) }
        }
    }

    fun dissociateExpenseFromUnits(expenseId: Int) {
        transaction {
            ExpenseUnitsTable.deleteWhere { expense eq EntityID(expenseId, ExpensesTable) }
        }
    }

    suspend fun getAll(): List<ExpenseUnit> = withContext(Dispatchers.IO) {
        transaction(database) {
            ExpenseUnitDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): ExpenseUnit = withContext(Dispatchers.IO) {
        transaction(database) {
            ExpenseUnitDAO[id].toEntity()
        }
    }

    suspend fun update(
        expenseUnit: ExpenseUnit
    ): ExpenseUnit = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                expenseUnit.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                expenseUnit.id.let { ExpenseUnitDAO.findById(it) }
            ) { "Нет единицы списания с id ${expenseUnit.id}" }

            expenseUnit.unit.let {
                val productUnitDao = requireNotNull(
                    it?.let { it1 -> ProductUnitDAO.findById(it1) }
                ) { "Нет товарной единицы с id ${expenseUnit.unit}" }
                dao.unit = productUnitDao
            }

            expenseUnit.expense.let {
                val expenseDao = requireNotNull(
                    it?.let { it1 -> ExpenseDAO.findById(it1) }
                ) { "Нет списания с id ${expenseUnit.expense}" }
                dao.expense = expenseDao
            }

            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                ExpenseUnitDAO.findById(id)
            ) { "Нет записи с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        expenseUnit: ExpenseUnit,
    ): ExpenseUnit = withContext(Dispatchers.IO) {
        val unit = requireNotNull(expenseUnit.unit) { "Не указана единица товара!" }
        val productUnitDAO = requireNotNull(
            ProductUnitDAO.findById(unit)
        ) { "Нет товарной единицы $unit" }
        val expense = requireNotNull(expenseUnit.expense) { "Не указано списание!" }
        val expenseDAO = requireNotNull(
            ExpenseDAO.findById(expense)
        ) { "Нет списания $expense" }

        transaction(database) {
            val dao = ExpenseUnitDAO.new {
                this.unit = productUnitDAO
                this.expense = expenseDAO
            }
            dao.toEntity()
        }
    }

    private fun ExpenseUnitDAO.toEntity() = ExpenseUnit(
        id = id.value,
        unit = unit.id.value,
        expense = expense.id.value
    )
}