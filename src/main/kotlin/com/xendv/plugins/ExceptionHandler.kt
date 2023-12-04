package com.xendv.plugins

import com.xendv.storeroom.entities.common.ServerException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptions() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            val error = ServerException(
                message = cause.message,
                type = cause::class.simpleName,
            )
            call.respond(HttpStatusCode.BadRequest, error)
        }
        exception<NoSuchElementException> { call, cause ->
            val error = ServerException(
                message = cause.message,
                type = cause::class.simpleName,
            )
            call.respond(HttpStatusCode.NotFound, error)
        }
    }
}