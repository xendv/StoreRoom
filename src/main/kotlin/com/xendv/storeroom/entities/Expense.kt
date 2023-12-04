package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    @SerialName("id") val id: Int?,
    @SerialName("units") val units: List<String>? = emptyList(),
    @SerialName("date") val date: String?,
)