package com.xendv.security.routes

import com.xendv.plugins.withRoles
import com.xendv.security.entities.UserEntity
import com.xendv.security.entities.UserRole
import com.xendv.security.entities.requests.LoginRequest
import com.xendv.security.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val authService by inject<AuthService>()

    route("/users") {
        authenticate {
            withRoles(UserRole.ADMIN) {
                get {
                    call.respond(authService.getAllUsers())
                }
                put {
                    val user = call.receive<UserEntity>()
                    call.respond(authService.createUser(user = user))
                }
                post {
                    val user = call.receive<UserEntity>()
                    call.respond(authService.updateUser(user = user))
                }
            }
        }
    }

    route("/auth") {
        post("login") {
            val request = call.receive<LoginRequest>()
            call.respond(authService.performLogin(request))
        }

        post("register") {
            val request = call.receive<UserEntity>()
            call.respond(authService.createUser(request, register = true))
        }
    }
}