package com.example.madcapstone.data.models.firebaseModels

/**
 * Data class for the OpeningHours object.
 * This class is used to store the opening hours of a location.
 *
 * @param openingTime The opening time of the location
 * @param closingTime The closing time of the location
 * @param isClosed A boolean indicating if the location is closed
 */
data class OpeningHours(
    val openingTime: String = "",
    val closingTime: String = "",
    @field:JvmField
    val isClosed: Boolean = false
)