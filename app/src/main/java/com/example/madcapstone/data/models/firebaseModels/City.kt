package com.example.madcapstone.data.models.firebaseModels

/**
 * Data class for the City object.
 * This class is used to store the city information of a location.
 * cities are stored in remote database (Firebase).
 * For now only canadians cities are stored.
 *
 * @param name The name of the city
 * @param stateName The name of the state the city is in
 * @param stateCode The code of the state the city is in
 * @param countryCode The code of the country the city is in
 * @param countryName The name of the country the city is in
 */
data class City(
    val name: String? = null,
    val stateName: String? = null,
    val stateCode: String? = null,
    val countryCode: String? = null,
    val countryName: String? = null,
)