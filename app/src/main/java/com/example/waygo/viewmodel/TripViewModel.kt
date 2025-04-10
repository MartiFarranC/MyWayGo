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

    fun editTrip(trip: Trip) {
        _trips.value = _trips.value.map { if (it.name == trip.name) trip else it }
        println("Trip editat: $trip")
    }

}