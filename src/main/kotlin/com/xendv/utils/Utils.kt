package com.xendv.utils

import io.ktor.server.config.*

operator fun ApplicationConfig.get(name: String): String {
    return property(name).getString()
}