package com.example.waygo.repository


import com.example.waygo.dao.TripDao
import com.example.waygo.entity.TripEntity
import com.example.waygo.model.Trip

class TripRepository(private val tripDao: TripDao){
    private val trips = mutableListOf<Trip>()

    suspend fun addTrip(trip: TripEntity) {
        tripDao.addTrip(trip)
    }

    suspend fun getTripById(id: Int): TripEntity? {
        return tripDao.getTripById(id)
    }

    suspend fun getAllTrips(): List<TripEntity> {
        return tripDao.getAllTrips()
    }

    suspend fun updateTrip(trip: TripEntity) {
        tripDao.updateTrip(trip)
    }

    suspend fun deleteTrip(trip: TripEntity) {
        tripDao.deleteTrip(trip)
    }

}