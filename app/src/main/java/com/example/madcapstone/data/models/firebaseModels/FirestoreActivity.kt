package com.example.madcapstone.data.models.firebaseModels

//TODO add more fields for activity detail
data class FirestoreActivity(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val websiteUrl: String = "",
    val Location: String = "",
    val address: String = "",
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val rating: Int = 0,
    val amountOfReviews: Int = 0,
    val monthlyVisitors: Int = 0,
    @field:JvmField
    val isFree: Boolean = false,
    val openingHours: Map<String, OpeningHours> = emptyMap(),
)
