package com.example.madcapstone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _cities = MutableLiveData<Resource<List<String>>>(Resource.Initial())
    val cities: LiveData<Resource<List<String>>>
        get() = _cities

    // search query as state flow, to observe changes and provide the debouncing functionality
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: MutableStateFlow<String>
        get() = _searchQuery

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    init {
        // observe search query and search cities when query changes
        viewModelScope.launch {
            _searchQuery.debounce(750L).collect() {
                searchCities(it)
            }
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            _cities.value = Resource.Loading()
            if (query.isBlank()) {
                _cities.value = Resource.Initial()
            } else {
                _cities.value = activityRepository.getPlaces(query)
            }
        }
    }

}