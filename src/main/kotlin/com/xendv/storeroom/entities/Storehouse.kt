package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Storehouse(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("address") val address: String?,
)