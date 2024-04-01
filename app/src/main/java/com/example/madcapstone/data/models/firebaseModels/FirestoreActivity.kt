package com.example.madcapstone.data.models.firebaseModels

import com.google.firebase.firestore.DocumentId

/**
 * Data class for the FirestoreActivity object.
 * This class is used to store the activity information of a location.
 *
 * @param id The id of the activity
 * @param name The name of the activity
 * @param description The description of the activity
 * @param imageUrl The url of the image of the activity
 * @param websiteUrl The url of the website of the activity
 * @param Location The location of the activity
 * @param address The address of the activity
 * @param minPrice The minimum price of the activity
 * @param maxPrice The maximum price of the activity
 * @param rating The rating of the activity
 * @param amountOfReviews The amount of reviews of the activity
 * @param monthlyVisitors The amount of monthly visitors of the activity
 * @param isFree A boolean that indicates if the activity is free
 * @param openingHours A map of the opening hours of the activity
 *
 * @property id The id of the activity
 */
data class FirestoreActivity(
    @DocumentId  val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val websiteUrl: String = "",
    val Location: String = "",
    val address: String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
    val rating: Int = 0,
    val amountOfReviews: Int = 0,
    val monthlyVisitors: Int = 0,
    @field:JvmField
    val isFree: Boolean = false,
    val openingHours: Map<String, OpeningHours> = emptyMap(),
)
