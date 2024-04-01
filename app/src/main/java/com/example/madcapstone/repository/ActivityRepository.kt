package com.example.madcapstone.repository

import android.util.Log
import com.example.madcapstone.data.models.firebaseModels.City
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.state.SearchFilterState
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

/**
 * Repository class for the activities.
 * This class is used to fetch activities from the Firestore database.
 *
 * @property store The Firestore database instance
 * @property cityRef The reference to the cities collection in the Firestore database
 * @property activityRef The reference to the activities collection in the Firestore database
 * @property NAME_REF The name of the activity
 *
 * @constructor Creates a new ActivityRepository
 */
class ActivityRepository {
    private val store = Firebase.firestore
    private val cityRef = store.collection("cities")
    private val activityRef = store.collection("activities")

    /**
     * Function to get the places from the Firestore database.
     *
     * @param query The query to search for
     * @return A Resource object containing the list of cities
     */
    suspend fun getPlaces(query: String): Resource<List<City>> {
        if (query.isBlank()) {
            return Resource.Initial()
        }

        val cities = mutableListOf<City>()
        //capitalize the first letter of the query
        val capitalizedQuery = query.replaceFirstChar { it.uppercase() }
        try {
            val response = cityRef
                .whereGreaterThanOrEqualTo(NAME_REF, capitalizedQuery)
                .whereLessThanOrEqualTo(NAME_REF, capitalizedQuery + "\uf8ff")
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

    /**
     * Function to get the viewed activities from the Firestore database.
     * This is a list of max 5 activities that where last viewed by the user.
     *
     * @param ids The ids of the activities to get
     * @return A Resource object containing the list of activities
     */
    suspend fun getViewedActivities(ids: Set<String>): Resource<List<FirestoreActivity>> {
        val activities = mutableListOf<FirestoreActivity>()
        try {
           val query = activityRef
                .whereIn(FieldPath.documentId(), ids.toList())
                .get()
                .await()

            for (document in query.documents) {
                val activity = document.toObject(FirestoreActivity::class.java)
                activity?.let {
                    activities.add(it)
                }
            }
        } catch (e: Exception) {
            return Resource.Error(
                e.localizedMessage ?: "An unknown error occurred while fetching viewed activities."
            )
        }

        if (activities.isEmpty()) {
            return Resource.Empty()
        }
        return Resource.Success(activities)
    }

    /**
     * Function to get the top activities from the Firestore database.
     *
     * @return A Resource object containing the list of activities
     */
   suspend fun getTopActivities(): Resource<List<FirestoreActivity>>{
        val activities = mutableListOf<FirestoreActivity>()
        try {
            val query = activityRef
                .orderBy(MONTHLY_VISITORS_REF, Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            for (document in query.documents) {
                val activity = document.toObject(FirestoreActivity::class.java)
                activity?.let {
                    activities.add(it)
                }
            }
        } catch (e: Exception) {
            return Resource.Error(
                e.localizedMessage ?: "An unknown error occurred while fetching top activities."
            )
        }

        if (activities.isEmpty()) {
            return Resource.Empty()
        }
        return Resource.Success(activities)
   }

    /**
     * Function to get the activities from the Firestore database.
     *
     * @param activityName The name of the activity to search for
     * @return A Resource object containing the list of activities
     */
    suspend fun getActivities(activityName: String): Resource<List<FirestoreActivity>> {
        if (activityName.isBlank()) {
            return Resource.Initial()
        }

        val activities = mutableListOf<FirestoreActivity>()
        val capitalizedQuery = activityName.replaceFirstChar { it.uppercase() }
        try {
            val response = activityRef
                .whereGreaterThanOrEqualTo(NAME_REF, capitalizedQuery)
                .whereLessThanOrEqualTo(NAME_REF, capitalizedQuery + "\uf8ff")
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


    /**
     * Function to get the activities from the Firestore database.
     *
     * @param cityName The name of the city to search for
     * @param filters The filters to apply to the search
     * @param limit The maximum amount of activities to return
     * @return A Resource object containing the list of activities
     */
    suspend fun getActivities(cityName: String, filters: SearchFilterState, limit: Long = 10): Resource<List<FirestoreActivity>> {
        if (cityName.isBlank()) {
            return Resource.Initial()
        }
        val activities = mutableListOf<FirestoreActivity>()
        try {
            var query = activityRef
                .whereEqualTo(LOCATION_REF, cityName)
                .limit(limit)

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

    /**
     * helper function to apply filters to the query
     *
     * @param query The query to apply the filters to
     * @param filters The filters to apply
     * @return The query with the filters applied
     */
    private fun applyFilters(query: Query, filters: SearchFilterState): Query {
        var localQuery = query
        filters.minPrice?.let { localQuery = localQuery.whereGreaterThanOrEqualTo(MAX_PRICE_REF, it) }
        filters.maxPrice?.let { localQuery = localQuery.whereLessThanOrEqualTo(MIN_PRICE_REF, it) }
        filters.minAmountOfVisitors?.let { localQuery = localQuery.whereGreaterThanOrEqualTo(
            MONTHLY_VISITORS_REF, it) }
        filters.minRating?.let { localQuery = localQuery.whereGreaterThanOrEqualTo(RATING_REF, it) }
        return localQuery
    }

    companion object {
        private const val NAME_REF = "name"
        private const val LOCATION_REF = "Location"
        private const val MONTHLY_VISITORS_REF = "monthlyVisitors"
        private const val RATING_REF = "rating"
        private const val MIN_PRICE_REF = "minPrice"
        private const val MAX_PRICE_REF = "maxPrice"
    }
}