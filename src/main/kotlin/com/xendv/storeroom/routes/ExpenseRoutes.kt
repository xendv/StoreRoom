package com.xendv.storeroom.routes

import com.xendv.storeroom.entities.Expense
import com.xendv.storeroom.services.ExpenseService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.expenseRoutes() {
    val service by inject<ExpenseService>()

    route("/expense") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            call.respond(service.get(requireNotNull(call.parameters["id"]).toInt()))
        }

        post {
            call.respond(service.create(expense = call.receive<Expense>()))
        }

        put {
            call.respond(service.update(call.receive<Expense>()))
        }

        delete("/{id}") {
            call.respond(service.delete(requireNotNull(call.parameters["id"]).toInt()))
        }
    }
}