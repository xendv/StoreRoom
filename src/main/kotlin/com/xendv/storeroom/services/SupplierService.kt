package com.xendv.storeroom.services

import com.xendv.storeroom.entities.Supplier
import com.xendv.storeroom.entities.dao.SupplierDAO
import com.xendv.storeroom.entities.db.SuppliersTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class SupplierService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(SuppliersTable)
        }
    }

    suspend fun getAll(): List<Supplier> = withContext(Dispatchers.IO) {
        transaction(database) {
            SupplierDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): Supplier = withContext(Dispatchers.IO) {
        transaction(database) {
            SupplierDAO[id].toEntity()
        }
    }

    suspend fun update(
        supplier: Supplier
    ): Supplier = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                supplier.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                SupplierDAO.findById(supplier.id)
            ) { "Нет записи с id $id" }
            supplier.inn?.let { dao.inn = it }
            supplier.name?.let { dao.name = it }
            supplier.requisites?.let { dao.requisites = it }
            supplier.email?.let { dao.email = it }
            supplier.phone?.let { dao.phone = it }
            supplier.address?.let { dao.address = it }
            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                SupplierDAO.findById(id)
            ) { "Нет записи с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun delete(
        inn: String
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val find = SupplierDAO.find { SuppliersTable.inn.eq(inn) }
            val existing = requireNotNull(find.firstOrNull()) {
                "Не найден поставщик с ИНН $inn"
            }
            check(find.count().toInt() == 1) {
                "Множественные ИНН $inn"
            }

            existing.delete()
            return@transaction true
        }
    }

    suspend fun create(
        supplier: Supplier,
    ): Supplier = withContext(Dispatchers.IO) {
        val inn = requireNotNull(supplier.inn) { "Не указан ИНН!" }
        val name = requireNotNull(supplier.name) { "Не указано название!" }

        transaction(database) {
            require(
                !SupplierDAO.find { SuppliersTable.inn eq inn }.any()
            ) { "Поставщик с ИНН $inn уже есть" }

            val dao = SupplierDAO.new {
                this.inn = inn
                this.name = name
                this.requisites = requisites
                this.email = email
                this.phone = phone
                this.address = address
            }

            dao.toEntity()
        }
    }

    private fun SupplierDAO.toEntity() = Supplier(
        id = id.value,
        inn = inn,
        name = name,
        requisites = requisites,
        email = email,
        phone = phone,
        address = address,
    )
}