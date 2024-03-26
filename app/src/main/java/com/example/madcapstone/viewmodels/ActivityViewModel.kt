package com.example.madcapstone.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.models.firebaseModels.City
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.ActivityRepository
import com.example.madcapstone.state.SearchFilterState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ActivityViewModel : ViewModel() {
    private val activityRepository = ActivityRepository()

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
            _searchQuery.debounce(750L).collect() {
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

}