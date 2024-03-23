package com.example.madcapstone.data.models

//TODO add more fields for activity detail
data class Activity(
    val name: String,
    val imageUrl: String,
    val place: String,
    val address: String,
    val minPrice: Double?,
    val maxPrice: Double?,
    val rating: Int,
    val amountOfReviews: Int,
    val monthlyVisitors: Int,
    val isFree : Boolean,

)
