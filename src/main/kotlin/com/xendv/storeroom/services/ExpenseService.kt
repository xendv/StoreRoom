package com.xendv.storeroom.services

import com.xendv.storeroom.entities.Expense
import com.xendv.storeroom.entities.dao.ExpenseDAO
import com.xendv.storeroom.entities.dao.ProductUnitDAO
import com.xendv.storeroom.entities.db.ExpensesTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class ExpenseService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(ExpensesTable)
        }
    }

    suspend fun getAll(): List<Expense> = withContext(Dispatchers.IO) {
        transaction(database) {
            ExpenseDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): Expense = withContext(Dispatchers.IO) {
        transaction(database) {
            ExpenseDAO[id].toEntity()
        }
    }

    @Suppress("UncheckedCast")
    suspend fun update(
        expense: Expense
    ): Expense = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                expense.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                ExpenseDAO.findById(expense.id)
            ) { "Нет записи с id $id" }
            //expense.units?.let { dao.units = it as SizedIterable<ProductUnitDAO> }
            expense.date?.let { dao.date = LocalDateTime.parse(it) }
            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                ExpenseDAO.findById(id)
            ) { "Нет записи о списании с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        expense: Expense,
    ): Expense = withContext(Dispatchers.IO) {
        val units = requireNotNull(expense.units) { "Не указаны товары!" }
        val date = requireNotNull(expense.date) { "Не указана дата!" }

        transaction(database) {
            val dao = ExpenseDAO.new {
                this.units = units.mapNotNull { unit -> ProductUnitDAO.findById(unit) }
                    .asIterable() as SizedIterable<ProductUnitDAO>
                this.date = LocalDateTime.parse(date)
            }

            dao.toEntity()
        }
    }

    private fun ExpenseDAO.toEntity() = Expense(
        id = id.value,
        units = units.map { it.id.value },
        date = date.toString()
    )
}