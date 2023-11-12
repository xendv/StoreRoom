package com.xendv

import com.xendv.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureExceptions()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
