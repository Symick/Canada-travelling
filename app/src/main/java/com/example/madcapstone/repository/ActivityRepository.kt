package com.example.madcapstone.repository

import androidx.compose.ui.text.capitalize
import androidx.lifecycle.LiveData
import com.example.madcapstone.data.models.City
import com.example.madcapstone.data.util.Resource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.Locale

class ActivityRepository {
    private val store = Firebase.firestore
    private val cityRef = store.collection("cities")
    private val NAME = "name"

    suspend fun getPlaces(query: String): Resource<List<City>> {
        if (query.isBlank()) {
            return Resource.Initial()
        }

        val cities = mutableListOf<City>()
        val capitalizedQuery = query.replaceFirstChar { it.uppercase() }
        try {
            val response = cityRef
                .whereGreaterThanOrEqualTo(NAME, capitalizedQuery)
                .whereLessThanOrEqualTo(NAME, capitalizedQuery + "\uf8ff")
                .limit(10)
                .get()
                .await()

            for (document in response.documents) {
                val city = document.toObject(City::class.java)
                // add city to list if not null
                city?.let {
                    cities.add(it)
                }
            }
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "An unknown error occurred while searching cities.")
        }

        if (cities.isEmpty()) {
            return Resource.Empty()
        }

        return Resource.Success(cities)
    }
}