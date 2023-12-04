package com.xendv.plugins

import com.xendv.security.routes.authRoutes
import com.xendv.storeroom.routes.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            authRoutes()
            expenseRoutes()
            expenseUnitRoutes()
            lotRoutes()
            placementTypeRoutes()
            productInventoryRoutes()
            productRoutes()
            productUnitRoutes()
            storehouseRoutes()
            supplierRoutes()
        }
        swaggerUI("/api/openapi") //TODO autogenerate openapi spec
    }
}