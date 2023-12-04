package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Supplier(
    @SerialName("id") val id: Int?,
    @SerialName("inn") val inn: String?,
    @SerialName("name") val name: String?,
    @SerialName("requisites") val requisites: String?,
    @SerialName("email") val email: String?,
    @SerialName("phone") val phone: String?,
    @SerialName("address") val address: String?
)