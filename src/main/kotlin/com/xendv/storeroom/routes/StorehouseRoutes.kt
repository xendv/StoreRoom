package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.Storehouse
import com.xendv.storeroom.services.StorehouseService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.storehouseRoutes() {
    val service by inject<StorehouseService>()

    route("/storehouse") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"]).toInt()))
        }

        post {
            call.respond(service.create(storehouse = call.receive<Storehouse>()))
        }

        put {
            call.respond(service.update(call.receive<Storehouse>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"]).toInt()))
        }
    }
}