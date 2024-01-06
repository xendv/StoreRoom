package com.xendv.di.modules

import com.xendv.security.services.AuthService
import com.xendv.security.services.JwtService
import com.xendv.storeroom.services.*
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::JwtService).withOptions { createdAtStart() }
    singleOf(::AuthService).withOptions { createdAtStart() }
    singleOf(::ProductService).withOptions { createdAtStart() }
    singleOf(::ProductUnitService).withOptions { createdAtStart() }
    singleOf(::SupplierService).withOptions { createdAtStart() }
    singleOf(::LotService).withOptions { createdAtStart() }
    singleOf(::ExpenseService).withOptions { createdAtStart() }
    singleOf(::ExpenseUnitService).withOptions { createdAtStart() }
}