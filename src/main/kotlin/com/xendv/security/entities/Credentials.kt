package com.xendv.security.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    @SerialName("login") val login: String?,
    @SerialName("token") val token: String?,
)
