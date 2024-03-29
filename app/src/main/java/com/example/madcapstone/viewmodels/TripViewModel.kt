package com.example.madcapstone.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.repository.TripRepository
import kotlinx.coroutines.launch
import java.util.Date

class TripViewModel(application: Application): AndroidViewModel(application) {
    private val tripRepository = TripRepository(application.applicationContext)

    val trips = tripRepository.getTrips()



    private val _selectedTrip = mutableStateOf<Trip?>(null)
    val selectedTrip: Trip?
        get() = _selectedTrip.value

    fun selectTrip(trip: Trip) {
        _selectedTrip.value = trip
    }

    fun insertTrip(trip: Trip) {
        viewModelScope.launch {
            tripRepository.insertTrip(trip)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripRepository.deleteTrip(trip)
        }
    }

    fun addActivityToTrip(trip: Trip, activity: RoomActivity, date: Date) {
        viewModelScope.launch {
            tripRepository.addActivityToTrip(trip, activity, date)
        }
    }

    fun getTripActivities(tripId: String, date: Date): LiveData<List<RoomActivity>> {
        return tripRepository.getTripActivities(tripId, date)
    }
}