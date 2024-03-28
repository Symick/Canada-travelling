package com.example.madcapstone.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.repository.TripRepository
import kotlinx.coroutines.launch

class TripViewModel(application: Application): AndroidViewModel(application) {
    private val tripRepository = TripRepository(application.applicationContext)

    val trips = tripRepository.getTrips()


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
}