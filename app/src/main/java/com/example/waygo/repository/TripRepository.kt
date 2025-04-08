package com.example.waygo.repository

import com.example.waygo.model.Trip

class TripRepository {
    private val trips = mutableListOf<Trip>()

    fun getTrips(): List<Trip> = trips

    fun addTrip(trip: Trip) {
        trips.add(trip)
    }
}