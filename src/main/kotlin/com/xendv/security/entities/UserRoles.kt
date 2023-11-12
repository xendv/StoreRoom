package com.xendv.security.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {

    @SerialName("ADMIN")
    ADMIN,

    @SerialName("USER")
    USER,
}