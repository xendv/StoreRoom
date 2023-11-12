package com.xendv.plugins

import com.xendv.security.routes.authRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            authRoutes()
        }
        swaggerUI("/api/openapi") //TODO autogenerate openapi spec
    }
}