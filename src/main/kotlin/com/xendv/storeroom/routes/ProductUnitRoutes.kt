package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.ProductUnit
import com.xendv.storeroom.services.ProductUnitService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productUnitRoutes() {
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

        put {
            call.respond(service.update(call.receive<ProductUnit>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"])))
        }
    }
}