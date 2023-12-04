package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.ProductInventory
import com.xendv.storeroom.services.ProductInventoryService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productInventoryRoutes() {
    val service by inject<ProductInventoryService>()

    route("/productInventory") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"]).toInt()))
        }

        post {
            call.respond(service.create(productInventory = call.receive<ProductInventory>()))
        }

        put {
            call.respond(service.update(call.receive<ProductInventory>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"]).toInt()))
        }
    }
}