package com.example.madcapstone.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.repository.TripActivityRepository
import kotlinx.coroutines.launch

class TripDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val tripActivityRepository: TripActivityRepository = TripActivityRepository(application.applicationContext)

    private val _selectedTripActivity = mutableStateOf<RoomActivity?>(null)
    val selectedTripActivity get() = _selectedTripActivity.value

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