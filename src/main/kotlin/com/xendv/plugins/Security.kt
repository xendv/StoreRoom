package com.xendv.plugins

import com.xendv.security.entities.UserRole
import com.xendv.security.services.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import kotlin.collections.set

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    val jwtService by inject<JwtService>()
    authentication {
        jwt {
            realm = jwtService.jwtRealm
            verifier(jwtService.verifier)
            validate { credential ->
                if (credential.payload.audience.contains(jwtService.jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun Route.withRoles(vararg roles: UserRole, build: Route.() -> Unit) {
    val route = createChild(object : RouteSelector() {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
            return RouteSelectorEvaluation.Transparent
        }
    })

    route.install(RoleAuthorizationPlugin) {
        roles(roles.toSet())
    }

    route.build()
}

class RoleBaseConfiguration {
    val requiredRoles = mutableSetOf<UserRole>()
    fun roles(roles: Set<UserRole>) {
        requiredRoles.addAll(roles)
    }
}

val RoleAuthorizationPlugin = createRouteScopedPlugin("RoleAuthorizationPlugin", ::RoleBaseConfiguration) {
    on(AuthenticationChecked) { call ->
        val principal = call.principal<JWTPrincipal>() ?: return@on
        val roles = principal.payload.getClaim("roles")
            .asList(String::class.java)
            .map { UserRole.valueOf(it) }
            .toSet()

        if (pluginConfig.requiredRoles.isNotEmpty() && roles.intersect(pluginConfig.requiredRoles).isEmpty()) {
            call.respondText("You don`t have access to this resource!", status = HttpStatusCode.Unauthorized)
        }
    }
}
