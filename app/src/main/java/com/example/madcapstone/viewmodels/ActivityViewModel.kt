package com.example.madcapstone.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.datastore.RecommendationsDataStore
import com.example.madcapstone.data.models.firebaseModels.City
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.ActivityRepository
import com.example.madcapstone.state.SearchFilterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


/**
 * View model for the screen working with activities.
 *
 * @param application The application
 *
 * @author Julian Kruithof
 */
@OptIn(FlowPreview::class)
class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val activityRepository = ActivityRepository()
    private val dataStore = RecommendationsDataStore(application)
    private val scope = CoroutineScope(Dispatchers.IO)

    // cities as live data to observe changes from firestore
    private val _cities = MutableLiveData<Resource<List<City>>>(Resource.Initial())
    val cities: LiveData<Resource<List<City>>>
        get() = _cities

    // search query as state flow, to observe changes and provide the debouncing functionality
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: MutableStateFlow<String>
        get() = _searchQuery

    /**
     * Update search query when user types in search bar
     */
    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Search cities when search query changes
     */
    init {
        viewModelScope.launch {
            _searchQuery.debounce(500L).collect {
                searchCities(it)
            }
        }
    }

    /**
     * Search cities based on query
     */
    private fun searchCities(query: String) {
        viewModelScope.launch {
            _cities.value = Resource.Loading()
            if (query.isBlank()) {
                _cities.value = Resource.Initial()
            } else {
                _cities.value = activityRepository.getPlaces(query.trim())
            }
        }
    }

    private val _selectedActivity = mutableStateOf(FirestoreActivity())
    val selectedActivity: FirestoreActivity
        get() = _selectedActivity.value

    fun setSelectedActivity(activity: FirestoreActivity) {
        _selectedActivity.value = activity
    }

    //firestore activities
    private val _activities = MutableLiveData<Resource<List<FirestoreActivity>>>(Resource.Initial())
    val activities: LiveData<Resource<List<FirestoreActivity>>>
        get() = _activities

    fun getActivities(cityName: String, filters: SearchFilterState) {
        if (cityName.isBlank()) {
            return
        }
        _activities.value = Resource.Loading()
        viewModelScope.launch {
            _activities.value = activityRepository.getActivities(cityName, filters)
        }
    }

    fun resetActivities() {
        _activities.value = Resource.Initial()
    }

    fun savePlaceRecommendation(place: String) {
        scope.launch {
            dataStore.savePlaceRecommendation(place)
        }
    }

    fun addActivityRecommendation(activity: String) {
        scope.launch {
            dataStore.addActivityRecommendation(activity)
        }
    }

}