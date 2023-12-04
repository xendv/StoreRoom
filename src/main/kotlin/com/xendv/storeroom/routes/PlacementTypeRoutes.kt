package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.PlacementType
import com.xendv.storeroom.services.PlacementTypeService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.placementTypeRoutes() {
    val service by inject<PlacementTypeService>()

    route("/placementType") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"]).toInt()))
        }

        post {
            call.respond(service.create(placementType = call.receive<PlacementType>()))
        }

        put {
            call.respond(service.update(call.receive<PlacementType>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"]).toInt()))
        }
    }
}