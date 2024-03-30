package com.example.madcapstone.repository

import com.example.madcapstone.data.models.firebaseModels.City
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.state.SearchFilterState
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class ActivityRepository {
    private val store = Firebase.firestore
    private val cityRef = store.collection("cities")
    private val activityRef = store.collection("activities")
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
            return Resource.Error(
                e.localizedMessage ?: "An unknown error occurred while searching cities."
            )
        }

        if (cities.isEmpty()) {
            return Resource.Empty()
        }
        return Resource.Success(cities)
    }

    suspend fun getActivities(activityName: String): Resource<List<FirestoreActivity>> {
        if (activityName.isBlank()) {
            return Resource.Initial()
        }

        val activities = mutableListOf<FirestoreActivity>()
        val capitalizedQuery = activityName.replaceFirstChar { it.uppercase() }
        try {
            val response = activityRef
                .whereGreaterThanOrEqualTo(NAME, capitalizedQuery)
                .whereLessThanOrEqualTo(NAME, capitalizedQuery + "\uf8ff")
                .limit(10)
                .get()
                .await()

            for (document in response.documents) {
                val activity = document.toObject(FirestoreActivity::class.java)
                // add activity to list if not null
                activity?.let {
                    activities.add(it)
                }
            }
        } catch (e: Exception) {
            return Resource.Error(
                e.localizedMessage ?: "An unknown error occurred while searching activities."
            )
        }

        if (activities.isEmpty()) {
            return Resource.Empty()
        }
        return Resource.Success(activities)
    }


    suspend fun getActivities(cityName: String, filters: SearchFilterState): Resource<List<FirestoreActivity>> {
        if (cityName.isBlank()) {
            return Resource.Initial()
        }
        val activities = mutableListOf<FirestoreActivity>()
        try {
            var query = activityRef
                .whereEqualTo("Location", cityName)
                .limit(10)

            query = applyFilters(query, filters)

            val response = query.get().await()

            for (document in response.documents) {
                val activity = document.toObject<FirestoreActivity>()
                // add activity to list if not null
                activity?.let {
                    activities.add(activity)
                }
            }

        } catch (e: Exception) {
            return Resource.Error(
                e.localizedMessage ?: "An unknown error occurred while searching activities."
            )
        }

        if (activities.isEmpty()) {
            return Resource.Empty()
        }
        return Resource.Success(activities)
    }

    private fun applyFilters(query: Query, filters: SearchFilterState): Query {
        var localQuery = query
        filters.minPrice?.let { localQuery = localQuery.whereGreaterThanOrEqualTo("maxPrice", it) }
        filters.maxPrice?.let { localQuery = localQuery.whereLessThanOrEqualTo("minPrice", it) }
        filters.minAmountOfVisitors?.let { localQuery = localQuery.whereGreaterThanOrEqualTo("monthlyVisitors", it) }
        filters.minRating?.let { localQuery = localQuery.whereGreaterThanOrEqualTo("rating", it) }
        return localQuery
    }
}