package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductUnit(
    @SerialName("id") val id: String?,
    @SerialName("sku") val sku: String?,
    @SerialName("lot") val lot: Int?,
)