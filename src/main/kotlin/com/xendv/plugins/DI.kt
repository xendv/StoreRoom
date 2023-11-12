package com.xendv.plugins

import com.xendv.di.modules.databaseModule
import com.xendv.di.modules.serviceModule
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        val ktorModule = module {
            single<Application> {
                this@configureDependencyInjection
            }
        }

        modules(
            ktorModule,
            databaseModule,
            serviceModule
        )
    }
}