package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lot(
    @SerialName("id") val id: Int?,
    @SerialName("sku") val sku: String?,
    @SerialName("supplier") val supplier: Int?,
    @SerialName("supplyDate") val supplyDate: String?,
    @SerialName("expirationDate") val expirationDate: String?,
)