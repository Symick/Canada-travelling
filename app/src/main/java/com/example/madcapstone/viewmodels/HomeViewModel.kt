package com.example.madcapstone.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.datastore.RecommendationsDataStore
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.ActivityRepository
import com.example.madcapstone.state.SearchFilterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

/**
 * View model for the home screen.
 *
 * @param application The application
 * @author Julian Kruithof
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = RecommendationsDataStore(application)
    private val activityRepository = ActivityRepository()

    private val _activityRecommendations = MutableLiveData<Resource<List<FirestoreActivity>>>(Resource.Initial())
    val activityRecommendations: LiveData<Resource<List<FirestoreActivity>>> get() = _activityRecommendations

    private val _placeActivityRecommendations = MutableLiveData<Resource<List<FirestoreActivity>>>(Resource.Initial())
    val placeActivityRecommendations: LiveData<Resource<List<FirestoreActivity>>> get() = _placeActivityRecommendations

    private val _topActivities = MutableLiveData<Resource<List<FirestoreActivity>>>(Resource.Initial())
    val topActivities: LiveData<Resource<List<FirestoreActivity>>> get() = _topActivities

    init {
        viewModelScope.launch {
            _placeActivityRecommendations.value = Resource.Loading()
            dataStore.placeRecommendation.collect { place ->
                _placeActivityRecommendations.value =
                    activityRepository.getActivities(place, SearchFilterState(), 5)
            }
        }
        viewModelScope.launch {
            _activityRecommendations.value = Resource.Loading()
            dataStore.activityRecommendations.collect { ids: Set<String> ->
                if (ids.isEmpty()) {
                    _activityRecommendations.value = Resource.Empty()
                } else {
                    _activityRecommendations.value = activityRepository.getViewedActivities(ids)
                }
            }
        }

        viewModelScope.launch {
            _topActivities.value = Resource.Loading()
            _topActivities.value = activityRepository.getTopActivities()
        }
    }

    val placeRecommendation = dataStore.placeRecommendation

}