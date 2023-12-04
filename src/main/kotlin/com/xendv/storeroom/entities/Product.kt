package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("sku") val sku: String?,
    @SerialName("barcode") val barcode: String?,
    @SerialName("name") val name: String?,
    @SerialName("description") val description: String?,
    @SerialName("measurement") val measurement: String?,
)