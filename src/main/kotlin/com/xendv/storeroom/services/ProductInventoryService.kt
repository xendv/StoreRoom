package com.xendv.storeroom.services

import com.xendv.storeroom.entities.ProductInventory
import com.xendv.storeroom.entities.dao.PlacementTypeDAO
import com.xendv.storeroom.entities.dao.ProductInventoryDAO
import com.xendv.storeroom.entities.dao.ProductUnitDAO
import com.xendv.storeroom.entities.db.ProductInventoriesTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ProductInventoryService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(ProductInventoriesTable)
        }
    }

    suspend fun getAll(): List<ProductInventory> = withContext(Dispatchers.IO) {
        transaction(database) {
            ProductInventoryDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): ProductInventory = withContext(Dispatchers.IO) {
        transaction(database) {
            ProductInventoryDAO[id].toEntity()
        }
    }

    suspend fun update(
        productInventory: ProductInventory
    ): ProductInventory = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                productInventory.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                productInventory.id.let { ProductInventoryDAO.findById(it) }
            ) { "Нет записи о размещении с id ${productInventory.id}" }

            productInventory.unit.let {
                val productUnitDao = requireNotNull(
                    it?.let { it1 -> ProductUnitDAO.findById(it1) }
                ) { "Нет единицы товара с id ${productInventory.unit}" }
                dao.unit = productUnitDao
            }

            dao.placement = requireNotNull(
                productInventory.placement
            ) { "Не задан placement" }

            productInventory.placementType?.let {
                val placementTypeDao = requireNotNull(
                    PlacementTypeDAO.findById(it)
                ) { "Нет типа размещения с id ${productInventory.placementType}" }
                dao.placementType = placementTypeDao
            }

            dao.status = requireNotNull(
                productInventory.status
            ) { "Не задан статус" }

            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                ProductInventoryDAO.findById(id)
            ) { "Нет записи с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        productInventory: ProductInventory,
    ): ProductInventory = withContext(Dispatchers.IO) {
        val unit = requireNotNull(productInventory.unit) { "Не указана единица товара!" }
        val productUnitDAO = requireNotNull(
            ProductUnitDAO.findById(unit)
        ) { "Нет единицы товара $unit" }
        val placement = requireNotNull(productInventory.placement) { "Не указано расположение!" }
        val placementType = requireNotNull(productInventory.placementType) { "Не указан тип размещения!" }
        val placementTypeDAO = requireNotNull(
            PlacementTypeDAO.findById(placementType)
        ) { "Нет типа размещения $placementType" }
        val status = requireNotNull(productInventory.status) { "Не указан статус!" }

        transaction(database) {
            val dao = ProductInventoryDAO.new {
                this.unit = productUnitDAO
                this.placement = placement
                this.placementType = placementTypeDAO
                this.status = status
            }

            dao.toEntity()
        }
    }

    private fun ProductInventoryDAO.toEntity() = ProductInventory(
        id = id.value,
        unit = unit.id.value,
        placement = placement,
        placementType = placementType.id.value,
        status = status
    )
}