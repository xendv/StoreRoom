package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName

data class ProductInventory(
    @SerialName("id") val id: Int?,
    @SerialName("unit") val unit: String?,
    @SerialName("placement") val placement: String?,// 3-4-5
    @SerialName("placementType") val placementType: Int?,
    @SerialName("status") val status: String?,// in stock, shipped
)