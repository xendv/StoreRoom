package com.xendv.storeroom.services

import com.xendv.storeroom.entities.Product
import com.xendv.storeroom.entities.dao.ProductDAO
import com.xendv.storeroom.entities.db.ProductsTable
import com.xendv.utils.RandomIdentifierGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ProductService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(ProductsTable)
        }
    }

    suspend fun getAll(): List<Product> = withContext(Dispatchers.IO) {
        transaction(database) {
            ProductDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(sku: String): Product = withContext(Dispatchers.IO) {
        transaction(database) {
            ProductDAO[sku].toEntity()
        }
    }

    suspend fun update(
        product: Product
    ): Product = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                product.sku
            ) { "Не задан id (sku)" }
            val dao = requireNotNull(
                ProductDAO.findById(product.sku)
            ) { "Нет записи с id (sku) $id" }
            product.barcode?.let { dao.barcode = it }
            product.name?.let { dao.name = it }
            product.description?.let { dao.description = it }
            product.measurement?.let { dao.measurement = it }
            dao.toEntity()
        }
    }

    suspend fun delete(
        sku: String
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                ProductDAO.findById(sku)
            ) { "Нет записи с id (sku) $sku" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        product: Product,
    ): Product = withContext(Dispatchers.IO) {
        val barcode = requireNotNull(product.barcode) { "Не указан штрих-код!" }
        val name = requireNotNull(product.name) { "Не указано название!" }
        val sku = product.sku ?: RandomIdentifierGenerator().uniqueIdentifier()

        transaction(database) {
            require(
                !ProductDAO.find { ProductsTable.id eq sku }.any()
            ) { "Товар с sku $sku уже есть" }

            require(
                !ProductDAO.find { ProductsTable.barcode eq barcode }.any()
            ) { "Товар со штрих-кодом $barcode уже есть" }

            require(
                !ProductDAO.find { ProductsTable.name eq name }.any()
            ) { "Товар $name уже есть" }

            val dao = ProductDAO.new(sku) {
                this.barcode = barcode
                this.name = name
                this.description = product.description
                this.measurement = product.measurement
            }

            dao.toEntity()
        }
    }

    private fun ProductDAO.toEntity() = Product(
        sku = id.value,
        barcode = barcode,
        name = name,
        description = description,
        measurement = measurement,
    )
}