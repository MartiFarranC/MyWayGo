package com.example.waygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.dao.TripDao
import com.example.waygo.entity.TripEntity
import com.example.waygo.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TripViewModel(private val tripDao: TripDao) : ViewModel() {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            val tripEntities = tripDao.getAllTrips()
            _trips.value = tripEntities.map { it.toTrip() }
        }
    }

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.addTrip(trip.toEntity())
            loadTrips()
        }
    }

    fun editTrip(updatedTrip: Trip) {
        viewModelScope.launch {
            tripDao.updateTrip(updatedTrip.toEntity())
            loadTrips()
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.deleteTrip(trip.toEntity())
            loadTrips()
        }
    }

    private fun TripEntity.toTrip(): Trip {
        return Trip(
            name = this.name,
            destinations = this.destinations,
            participants = this.participants,
            startDate = this.startDate,
            endDate = this.endDate
        )
    }

    private fun Trip.toEntity(): TripEntity {
        return TripEntity(
            id = 0, // Replace with actual ID if available
            name = this.name,
            destinations = this.destinations,
            participants = this.participants,
            startDate = this.startDate,
            endDate = this.endDate,
        )
    }

    private fun calculateDurationDays(startDate: String, endDate: String): Int {
        // Implement logic to calculate duration in days
        return 0 // Placeholder
    }
}