package com.example.waygo.viewmodel

import com.example.waygo.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel

class TripViewModel : ViewModel() {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    fun addTrip(trip: Trip) {
        _trips.value = _trips.value + trip
        println("Trip afegit: $trip")
    }

    fun editTrip(updatedTrip: Trip) {
        val currentTrips = trips.value.toMutableList()
        val index = currentTrips.indexOfFirst { it.name == updatedTrip.name }
        if (index != -1) {
            currentTrips[index] = updatedTrip
            _trips.value = currentTrips
        }
    }

    fun deleteTrip(trip: Trip) {
        _trips.value = _trips.value.filter { it.name != trip.name }
        println("Trip esborrat: $trip")
    }
}