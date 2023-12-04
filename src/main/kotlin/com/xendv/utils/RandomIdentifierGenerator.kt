package com.xendv.utils

import com.xendv.storeroom.entities.db.ProductsTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

class RandomIdentifierGenerator(
    private val seed: Long = System.currentTimeMillis(),
    private val length: Int = 10,
    private val maxRetries: Int = 5
) {
    companion object {
        val possibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    }

    private fun randomString(): String {
        return (1..length)
            .map { possibleCharacters.random(Random(seed)) }
            .joinToString("")
    }

    private fun identifierExists(identifier: String): Boolean {
        return transaction {
            !ProductsTable.select { ProductsTable.id eq identifier }.empty()
        }
    }

    fun uniqueIdentifier(): String? {
        var retries = 0

        while (retries < maxRetries) {
            val generatedIdentifier = randomString()

            if (!identifierExists(generatedIdentifier)) {
                return generatedIdentifier
            }

            retries++
        }

        return null
    }
}