package com.xendv.storeroom.services

import com.xendv.storeroom.entities.Storehouse
import com.xendv.storeroom.entities.dao.StorehouseDAO
import com.xendv.storeroom.entities.db.StorehouseTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class StorehouseService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(StorehouseTable)
        }
    }

    suspend fun getAll(): List<Storehouse> = withContext(Dispatchers.IO) {
        transaction(database) {
            StorehouseDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): Storehouse = withContext(Dispatchers.IO) {
        transaction(database) {
            StorehouseDAO[id].toEntity()
        }
    }

    suspend fun update(
        storehouse: Storehouse
    ): Storehouse = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                storehouse.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                StorehouseDAO.findById(storehouse.id)
            ) { "Нет записи с id $id" }
            storehouse.name?.let { dao.name = it }
            storehouse.address?.let { dao.address = it }
            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                StorehouseDAO.findById(id)
            ) { "Нет склада с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        storehouse: Storehouse,
    ): Storehouse = withContext(Dispatchers.IO) {
        val name = requireNotNull(storehouse.name) { "Не указано название!" }
        val address = requireNotNull(storehouse.address) { "Не указан адрес!" }

        transaction(database) {
            require(
                !StorehouseDAO.find { StorehouseTable.name eq name }.any()
            ) { "Склад $name уже есть" }

            val dao = StorehouseDAO.new {
                this.name = name
                this.address = address
            }

            dao.toEntity()
        }
    }

    private fun StorehouseDAO.toEntity() = Storehouse(
        id = id.value,
        name = name,
        address = address
    )
}