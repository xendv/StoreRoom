package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.Product
import com.xendv.storeroom.services.ProductService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productRoutes() {
    val service by inject<ProductService>()

    route("/product") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            val id = requireNotNull(call.parameters["id"])
            call.respond(service.get(id))
        }

        post {
            val product = call.receive<Product>()
            call.respond(service.create(product = product))
        }

        put {
            val product = call.receive<Product>()
            call.respond(service.update(product))
        }

        delete("/{id}") {
            val id = requireNotNull(call.parameters["id"])
            call.respond(service.delete(id))
        }
    }
}