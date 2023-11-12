package com.xendv.security.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    @SerialName("id") val id: Int?,
    @SerialName("login") val login: String?,
    @SerialName("password") val password: String? = null,
    @SerialName("roles") val roles: Set<UserRole>? = emptySet(),
)