package com.xendv.storeroom.services

import com.xendv.storeroom.entities.PlacementType
import com.xendv.storeroom.entities.dao.PlacementTypeDAO
import com.xendv.storeroom.entities.db.PlacementTypesTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PlacementTypeService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(PlacementTypesTable)
        }
    }

    suspend fun getAll(): List<PlacementType> = withContext(Dispatchers.IO) {
        transaction(database) {
            PlacementTypeDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): PlacementType = withContext(Dispatchers.IO) {
        transaction(database) {
            PlacementTypeDAO[id].toEntity()
        }
    }

    suspend fun update(
        placementType: PlacementType
    ): PlacementType = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                placementType.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                PlacementTypeDAO.findById(placementType.id)
            ) { "Нет записи с id $id" }
            placementType.name?.let { dao.name = it }
            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                PlacementTypeDAO.findById(id)
            ) { "Нет записи с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        placementType: PlacementType,
    ): PlacementType = withContext(Dispatchers.IO) {
        val name = requireNotNull(placementType.name) { "Не указано название!" }

        transaction(database) {
            require(
                !PlacementTypeDAO.find { PlacementTypesTable.name eq name }.any()
            ) { "Тип $name уже есть" }

            val dao = PlacementTypeDAO.new {
                this.name = name
            }

            dao.toEntity()
        }
    }

    private fun PlacementTypeDAO.toEntity() = PlacementType(
        id = id.value,
        name = name,
    )
}