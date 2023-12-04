package com.xendv.security.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.xendv.security.entities.dao.UserDAO
import com.xendv.utils.service.get
import io.ktor.server.application.*
import java.util.*

class JwtService(
    application: Application,
) {
    private val jwtDomain = application.environment.config["jwt.domain"]
    private val jwtSecret = application.environment.config["jwt.secret"]

    val jwtRealm = application.environment.config["jwt.realm"]
    val jwtAudience = application.environment.config["jwt.audience"]

    val verifier: JWTVerifier by lazy {
        JWT.require(Algorithm.HMAC256(jwtSecret))
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .build()
    }

    fun createToken(user: UserDAO): String = JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim("login", user.login)
        .withClaim("roles", user.roles.map { it.role.role.name })
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(jwtSecret))
}