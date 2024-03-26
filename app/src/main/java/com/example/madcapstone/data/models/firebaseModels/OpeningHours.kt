package com.example.madcapstone.data.models.firebaseModels

data class OpeningHours(
    val openingTime: String = "",
    val closingTime: String = "",
    @field:JvmField
    val isClosed: Boolean = false
)