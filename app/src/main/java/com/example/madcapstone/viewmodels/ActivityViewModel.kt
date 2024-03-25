package com.example.madcapstone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.models.City
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.ActivityRepository
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

}