package com.xendv.storeroom.services

import com.xendv.storeroom.entities.Lot
import com.xendv.storeroom.entities.dao.LotDAO
import com.xendv.storeroom.entities.dao.ProductDAO
import com.xendv.storeroom.entities.dao.SupplierDAO
import com.xendv.storeroom.entities.db.LotsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class LotService(
    private val database: Database,
) {
    init {
        transaction(database) {
            SchemaUtils.create(LotsTable)
        }
    }

    suspend fun getAll(): List<Lot> = withContext(Dispatchers.IO) {
        transaction(database) {
            LotDAO.all().map { it.toEntity() }
        }
    }

    suspend fun get(id: Int): Lot = withContext(Dispatchers.IO) {
        transaction(database) {
            LotDAO[id].toEntity()
        }
    }

    suspend fun update(
        lot: Lot
    ): Lot = withContext(Dispatchers.IO) {
        transaction(database) {
            requireNotNull(
                lot.id
            ) { "Не задан id" }
            val dao = requireNotNull(
                LotDAO.findById(lot.id)
            ) { "Нет записи с id $id" }
            val unitDao = requireNotNull(
                lot.sku?.let { ProductDAO.findById(it) }
            ) { "Нет записи с id (sku) $id" }
            lot.sku?.let { dao.sku = unitDao }
            val supplierDao = requireNotNull(
                lot.supplier?.let { SupplierDAO.findById(it) }
            ) { "Нет записи с id (sku) $id" }
            lot.supplier?.let { dao.supplier = supplierDao }
            lot.supplyDate?.let { dao.supplyDate = LocalDateTime.parse(it) }
            lot.expirationDate?.let { dao.expirationDate = LocalDateTime.parse(it) }
            dao.toEntity()
        }
    }

    suspend fun delete(
        id: Int
    ): Boolean = withContext(Dispatchers.IO) {
        transaction(database) {
            val dao = requireNotNull(
                LotDAO.findById(id)
            ) { "Нет записи о поставке с id $id" }

            dao.delete()
            return@transaction true
        }
    }

    suspend fun create(
        lot: Lot,
    ): Lot = withContext(Dispatchers.IO) {
        val sku = requireNotNull(lot.sku) { "Не указан SKU!" }
        val productDao = requireNotNull(
            ProductDAO.findById(sku)
        ) { "Нет товара с sku $sku" }
        val supplier = requireNotNull(lot.supplier) { "Не указан поставщик!" }
        val supplierDao = requireNotNull(
            SupplierDAO.findById(supplier)
        ) { "Нет поставщика с id $supplier" }
        val supplyDate = requireNotNull(lot.supplyDate) { "Не указана дата поставки!" }

        transaction(database) {
            val dao = LotDAO.new {
                this.sku = productDao
                this.supplier = supplierDao
                this.supplyDate = LocalDateTime.parse(supplyDate)
                this.expirationDate = if (lot.expirationDate != null) LocalDateTime.parse(lot.expirationDate) else null
            }

            dao.toEntity()
        }
    }

    private fun LotDAO.toEntity() = Lot(
        id = id.value,
        sku = sku.id.value,
        supplier = supplier.id.value,
        supplyDate = supplyDate.toString(),
        expirationDate = expirationDate.toString()
    )
}