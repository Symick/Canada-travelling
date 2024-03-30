package com.example.madcapstone.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.ActivityRepository
import com.example.madcapstone.repository.TripActivityRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class TripDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val tripActivityRepository: TripActivityRepository = TripActivityRepository(application.applicationContext)
    private val activityRepository = ActivityRepository()

    private val _selectedTripActivity = mutableStateOf<RoomActivity?>(null)
    val selectedTripActivity get() = _selectedTripActivity.value

    private val _foundActivities = MutableLiveData<Resource<List<FirestoreActivity>>>(Resource.Initial())
    val foundActivities get() = _foundActivities

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: MutableStateFlow<String>
        get() = _searchQuery

    /**
     * Update search query when user types in search bar
     */
    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    init {
        viewModelScope.launch {
            _searchQuery.debounce(500L).collect {
                searchActivities(it)
            }
        }
    }


    private fun searchActivities(query: String) {
        viewModelScope.launch {
            _foundActivities.value = Resource.Loading()
            if (query.isBlank()) {
                _foundActivities.value = Resource.Initial()
            } else {
                _foundActivities.value = activityRepository.getActivities(query.trim())
            }
        }
    }

    fun setSelectedTripActivity(roomActivity: RoomActivity) {
        _selectedTripActivity.value = roomActivity
    }

    fun deleteTripActivity(tripActivity: TripActivity) {
        viewModelScope.launch {
            tripActivityRepository.deleteTripActivity(tripActivity)
        }
    }

    fun updateTripActivity(tripActivity: TripActivity) {
        viewModelScope.launch {
            tripActivityRepository.updateTripActivity(tripActivity)
        }
    }

}