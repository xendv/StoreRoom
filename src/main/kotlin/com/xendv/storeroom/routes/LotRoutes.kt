package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.Lot
import com.xendv.storeroom.services.LotService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.lotRoutes() {
    val service by inject<LotService>()

    route("/lot") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"]).toInt()))
        }

        post {
            call.respond(service.create(lot = call.receive<Lot>()))
        }

        put {
            call.respond(service.update(call.receive<Lot>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"]).toInt()))
        }
    }
}