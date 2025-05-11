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

    private var isGuest: Boolean = false
    private var guestTrips: MutableList<Trip> = mutableListOf()

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
            if (isGuest) {
                guestTrips.add(trip)
                _trips.value = guestTrips.toList()
            } else {
                tripDao.addTrip(trip.toEntity())
                loadTripsByUserId(trip.userId)
            }
        }
    }

    fun editTrip(updatedTrip: Trip) {
        viewModelScope.launch {
            if (isGuest) {
                guestTrips = guestTrips.map {
                    if (it.id == updatedTrip.id) updatedTrip else it
                }.toMutableList()
                _trips.value = guestTrips.toList()
            } else {
                tripDao.updateTrip(updatedTrip.toEntity())
                loadTripsByUserId(updatedTrip.userId)
            }
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            if (isGuest) {
                guestTrips.removeAll { it.id == trip.id }
                _trips.value = guestTrips.toList()
            } else {
                tripDao.deleteTrip(trip.toEntity())
                loadTripsByUserId(trip.userId)
            }
        }
    }

    fun loadTripsByUserId(userId: String) {
        viewModelScope.launch {
            if (userId == "guest") {
                isGuest = true
                _trips.value = guestTrips
            } else {
                isGuest = false
                val tripEntities = tripDao.getTripsByUserId(userId)
                _trips.value = tripEntities.map { it.toTrip() }
            }
        }
    }

    private fun TripEntity.toTrip(): Trip {
        return Trip(
            id = this.id,
            name = this.name,
            destinations = this.destinations,
            participants = this.participants,
            startDate = this.startDate,
            endDate = this.endDate,
            userId = this.userId
        )
    }

    private fun Trip.toEntity(): TripEntity {
        return TripEntity(
            id = this.id,
            name = this.name,
            destinations = this.destinations,
            participants = this.participants,
            startDate = this.startDate,
            endDate = this.endDate,
            userId = this.userId
        )
    }
}