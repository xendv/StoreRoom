package com.xendv.di.modules

import com.xendv.security.services.AuthService
import com.xendv.security.services.JwtService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::JwtService)
    singleOf(::AuthService)
}