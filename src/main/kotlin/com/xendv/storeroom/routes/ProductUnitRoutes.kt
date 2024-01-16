package com.xendv.storeroom.routes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xendv.storeroom.entities.ProductUnit
import com.xendv.storeroom.services.ProductUnitService
import com.xendv.utils.QRCodeGenerator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productUnitRoutes(qrCodeGenerator: QRCodeGenerator) {
    val service by inject<ProductUnitService>()

    route("/productUnit") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"])))
        }

        post {
            call.respond(service.create(productUnit = call.receive<ProductUnit>()))
        }

        post("/qrCodes") {
            val json = call.receiveText()
            val contents = parseJsonArray(json)
            if (contents.isNotEmpty()) {
                qrCodeGenerator.generateMultipleQRCodes(contents)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Ошибка генерации")
            }
        }

        put {
            call.respond(service.update(call.receive<ProductUnit>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"])))
        }
    }
}

fun parseJsonArray(json: String): List<String> {
    return try {
        val mapper = jacksonObjectMapper()
        mapper.readValue<List<String>>(json)
    } catch (e: Exception) {
        emptyList()
    }
}