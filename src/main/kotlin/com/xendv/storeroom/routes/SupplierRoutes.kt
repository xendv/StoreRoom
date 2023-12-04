package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.Supplier
import com.xendv.storeroom.services.SupplierService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.supplierRoutes() {
    val service by inject<SupplierService>()

    route("/supplier") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"]).toInt()))
        }

        post {
            call.respond(service.create(supplier = call.receive<Supplier>()))
        }

        put {
            call.respond(service.update(call.receive<Supplier>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"]).toInt()))
        }
    }
}