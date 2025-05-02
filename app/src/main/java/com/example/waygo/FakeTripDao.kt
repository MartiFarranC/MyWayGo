package com.example.waygo.dao

import com.example.waygo.entity.TripEntity
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FakeTripDao : TripDao {
    private val trips = mutableListOf<TripEntity>()
    private val mutex = Mutex()

    override suspend fun addTrip(trip: TripEntity) {
        mutex.withLock {
            trips.add(trip)
        }
    }

    override suspend fun getTripById(id: Int): TripEntity? {
        return mutex.withLock {
            trips.find { it.id == id }
        }
    }

    override suspend fun getAllTrips(): List<TripEntity> {
        return mutex.withLock {
            trips.toList()
        }
    }

    override suspend fun updateTrip(trip: TripEntity) {
        mutex.withLock {
            val index = trips.indexOfFirst { it.id == trip.id }
            if (index != -1) {
                trips[index] = trip
            }
        }
    }

    override suspend fun deleteTrip(trip: TripEntity) {
        mutex.withLock {
            trips.removeIf { it.id == trip.id } 
        }
    }
}