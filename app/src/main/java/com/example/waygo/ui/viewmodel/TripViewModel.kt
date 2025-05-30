package com.example.waygo.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.data.local.dao.TripDao
import com.example.waygo.data.local.entity.TripEntity
import com.example.waygo.domain.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val TAG_VM = "TripViewModel"

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
            Log.d(TAG_VM, "Loading all trips from database...")
            val tripEntities = tripDao.getAllTrips()
            _trips.value = tripEntities.map { it.toTrip() }
            Log.d(TAG_VM, "Loaded ${_trips.value.size} trips from database.")
            _trips.value.forEach { trip ->
                Log.d(TAG_VM, "Initial Load: Trip ID: ${trip.id}, Images Count: ${trip.images.size}")
                trip.images.forEachIndexed { index, uri ->
                    Log.d(TAG_VM, "  Image $index URI: $uri")
                }
            }
        }
    }

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            Log.d(TAG_VM, "Attempting to add trip: ${trip.name}")
            if (isGuest) {
                guestTrips.add(trip)
                _trips.value = guestTrips.toList()
                Log.d(TAG_VM, "Added trip as guest. Total guest trips: ${_trips.value.size}")
            } else {
                val newId = tripDao.addTrip(trip.toEntity())
                Log.d(TAG_VM, "Added trip to DB. New ID: $newId. Reloading trips by user ID for full consistency.")
                loadTripsByUserId(trip.userId)
            }
        }
    }

    fun editTrip(updatedTrip: Trip) {
        viewModelScope.launch {
            Log.d(TAG_VM, "Attempting to edit trip ID: ${updatedTrip.id}, Images Count: ${updatedTrip.images.size}")
            if (isGuest) {
                guestTrips = guestTrips.map {
                    if (it.id == updatedTrip.id) updatedTrip else it
                }.toMutableList()
                _trips.value = guestTrips.toList()
                Log.d(TAG_VM, "Edited trip as guest. Updated guest trip count: ${_trips.value.size}")
            } else {
                tripDao.updateTrip(updatedTrip.toEntity())
                Log.d(TAG_VM, "Edited trip in DB. Reloading trips by user ID.")
                loadTripsByUserId(updatedTrip.userId)
            }
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            Log.d(TAG_VM, "Attempting to delete trip ID: ${trip.id}")
            if (isGuest) {
                guestTrips.removeAll { it.id == trip.id }
                _trips.value = guestTrips.toList()
                Log.d(TAG_VM, "Deleted trip as guest. Remaining guest trips: ${_trips.value.size}")
            } else {
                tripDao.deleteTrip(trip.toEntity())
                Log.d(TAG_VM, "Deleted trip from DB. Reloading trips by user ID.")
                loadTripsByUserId(trip.userId)
            }
        }
    }

    fun loadTripsByUserId(userId: String) {
        viewModelScope.launch {
            Log.d(TAG_VM, "Loading trips for user ID: $userId")
            if (userId == "guest") {
                isGuest = true
                _trips.value = guestTrips.toList() // Ensure a new list object is always emitted
                Log.d(TAG_VM, "Loaded guest trips. Count: ${_trips.value.size}")
            } else {
                isGuest = false
                val tripEntities = tripDao.getTripsByUserId(userId)
                _trips.value = tripEntities.map { it.toTrip() } // map creates a new list
                Log.d(TAG_VM, "Loaded authenticated user trips. Count: ${_trips.value.size}")
            }
            _trips.value.forEach { trip ->
                Log.d(TAG_VM, "After loadTripsByUserId (forced reload): Trip ID: ${trip.id}, Images Count: ${trip.images.size}")
                trip.images.forEachIndexed { index, uri ->
                    Log.d(TAG_VM, "  Image $index URI: $uri")
                }
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
            userId = this.userId,
            images = this.images
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
            userId = this.userId,
            images = this.images
        )
    }

    fun getTripById(tripId: Int?): Trip? {
        val trip = trips.value.find { it.id == tripId }
        Log.d(TAG_VM, "getTripById($tripId) returned: ${trip?.name}, Images Count: ${trip?.images?.size ?: 0}")
        return trip
    }

    fun addImageToTrip(tripId: Int, uri: Uri) {
        viewModelScope.launch {
            Log.d(TAG_VM, "addImageToTrip called with tripId = $tripId and uri = $uri")

            val updatedTrips = _trips.value.map { trip ->
                if (trip.id == tripId) {
                    Log.d(TAG_VM, ">> MATCH FOUND: Updating trip '${trip.name}' with new image")
                    val updatedImages = trip.images + uri
                    trip.copy(images = updatedImages)
                } else trip
            }
            _trips.value = updatedTrips

            val tripToUpdate = updatedTrips.find { it.id == tripId }
            if (tripToUpdate != null) {
                Log.d(TAG_VM, " tripToUpdate FOUND: ${tripToUpdate.name}")

                if (!isGuest) {
                    tripDao.updateTrip(tripToUpdate.toEntity())
                    Log.d(TAG_VM, " tripDao.updateTrip called, reloading from DB...")
                    loadTripsByUserId(tripToUpdate.userId)
                }
            } else {
                Log.w(TAG_VM, "tripToUpdate NOT FOUND for tripId = $tripId")
            }
        }
    }




    fun removeImageFromTrip(tripId: Int, uri: Uri) {
        viewModelScope.launch {
            val updatedTrips = _trips.value.map { trip ->
                if (trip.id == tripId) {
                    val updatedImages = trip.images.toMutableList().apply { remove(uri) }
                    trip.copy(images = updatedImages)
                } else {
                    trip
                }
            }
            _trips.value = updatedTrips

            val tripToUpdate = updatedTrips.find { it.id == tripId }
            if (!isGuest && tripToUpdate != null) {
                tripDao.updateTrip(tripToUpdate.toEntity())
            }
        }
    }
}
