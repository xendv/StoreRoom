package com.xendv.storeroom.services

import com.xendv.storeroom.entities.ProductUnit
import com.xendv.storeroom.entities.dao.LotDAO
import com.xendv.storeroom.entities.dao.ProductDAO
import com.xendv.storeroom.entities.dao.ProductUnitDAO
import com.xendv.storeroom.entities.db.ProductUnitsTable
import com.xendv.utils.RandomIdentifierGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ProductUnitService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(ProductUnitsTable)
        }
    }

    suspend fun getAll(): List<ProductUnit> = withContext(Dispatchers.IO) {
        transaction(database) {
            ProductUnitDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: String): ProductUnit = withContext(Dispatchers.IO) {
        transaction(database) {
            ProductUnitDAO[id].toEntity()
        }
    }

    suspend fun update(
        productUnit: ProductUnit
    ): ProductUnit = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                productUnit.sku
            ) { "Не задан id" }
            val dao = requireNotNull(
                productUnit.id?.let { ProductUnitDAO.findById(it) }
            ) { "Нет единицы товара с id ${productUnit.lot}" }

            productUnit.sku.let {
                val productDao = requireNotNull(
                    ProductDAO.findById(it)
                ) { "Нет товара с id (sku) ${productUnit.sku}" }
                dao.sku = productDao
            }

            productUnit.lot?.let {
                val lotDao = requireNotNull(
                    LotDAO.findById(it)
                ) { "Нет поставки с id ${productUnit.lot}" }
                dao.lot = lotDao
            }
            dao.toEntity()
        }
    }

    suspend fun delete(
        id: String
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                ProductUnitDAO.findById(id)
            ) { "Нет записи с id (sku) $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        productUnit: ProductUnit,
    ): ProductUnit = withContext(Dispatchers.IO) {
        val id = productUnit.id ?: RandomIdentifierGenerator().uniqueIdentifier()

        val sku = requireNotNull(productUnit.sku) { "Не указан sku!" }
        val productDao = requireNotNull(
            ProductDAO.findById(sku)
        ) { "Нет товара с sku $sku" }
        val lot = requireNotNull(productUnit.lot) { "Не указана поставка!" }
        val lotDao = requireNotNull(
            LotDAO.findById(lot)
        ) { "Нет поставки с id ${productUnit.lot}" }

        transaction(database) {
            val dao = ProductUnitDAO.new(id) {
                this.sku = productDao
                this.lot = lotDao
            }

            dao.toEntity()
        }
    }

    private fun ProductUnitDAO.toEntity() = ProductUnit(
        id = id.value,
        sku = id.value,
        lot = lot.id.value,
    )
}