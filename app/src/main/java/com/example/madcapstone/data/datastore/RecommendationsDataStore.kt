package com.example.madcapstone.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class RecommendationsDataStore(private val context: Context) {


    suspend fun savePlaceRecommendation(place: String) {
        context.dataStore.edit { preferences ->
            preferences[PLACE_RECOMMENDATION_KEY] = place
        }
    }

    suspend fun addActivityRecommendation(activity: String) {
        context.dataStore.edit { preferences ->
            val currentRecommendations = preferences[ACTIVITY_RECOMMENDATION_KEY] ?: setOf()
            val newRecommendations = currentRecommendations.toMutableSet()
            newRecommendations.add(activity)
            if (newRecommendations.size > MAX_ACTIVITY_RECOMMENDATIONS) {
                newRecommendations.remove(newRecommendations.first())
            }
            preferences[ACTIVITY_RECOMMENDATION_KEY] = newRecommendations
        }
    }

    val placeRecommendation = context.dataStore.data.map { preferences ->
        preferences[PLACE_RECOMMENDATION_KEY] ?: "Banff"
    }

    val activityRecommendations = context.dataStore.data.map { preferences ->
        preferences[ACTIVITY_RECOMMENDATION_KEY] ?: setOf()
    }

    companion object {
        private const val TAG = "RecommendationsDataStore"
        val Context.dataStore by preferencesDataStore(name = TAG)
        private val PLACE_RECOMMENDATION_KEY = stringPreferencesKey("place_recommendation")
        private val ACTIVITY_RECOMMENDATION_KEY = stringSetPreferencesKey("activity_recommendation")
        private const val MAX_ACTIVITY_RECOMMENDATIONS = 5
    }



}