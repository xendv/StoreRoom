package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseUnit(
    @SerialName("id") val id: Int?,
    @SerialName("expense") val expense: Int?,
    @SerialName("unit") val unit: String?,
)