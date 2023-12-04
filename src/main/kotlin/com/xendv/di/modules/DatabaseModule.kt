package com.xendv.di.modules

import com.xendv.utils.service.get
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val databaseModule = module {

    single<Database>(createdAtStart = true) {
        val environment = get<Application>().environment
        Database.connect(
            url = environment.config["postgres.url"],
            user = environment.config["postgres.user"],
            driver = environment.config["postgres.driver"],
            password = environment.config["postgres.password"],
        )
    }
}