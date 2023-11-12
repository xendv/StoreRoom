package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerException(
    @SerialName("message") val message: String?,
    @SerialName("type") val type: String?,
)
