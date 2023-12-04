package com.xendv.storeroom.entities

import kotlinx.serialization.SerialName

data class PlacementType(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,// ячейка, коробка, палета
)