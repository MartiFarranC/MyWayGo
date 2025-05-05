package com.example.waygo.dao

import android.util.Log
import androidx.room.*
import com.example.waygo.entity.TripEntity

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrip(trip: TripEntity)
//    {
//        Log.d("TripDao", "Adding trip: $trip")
//    }

    @Query("SELECT * FROM TripEntity WHERE id = :id")
    suspend fun getTripById(id: Int): TripEntity? //TODO: Log.d("TripDao", "Fetching trip with ID: $id")

    @Query("SELECT * FROM TripEntity ORDER BY startDate ASC")
    suspend fun getAllTrips(): List<TripEntity> //TODO: Log.d("TripDao", "Fetching all trips")

    @Update
    suspend fun updateTrip(trip: TripEntity)
//    {
//        Log.d("TripDao", "Updating trip: $trip")
//    }

    @Delete
    suspend fun deleteTrip(trip: TripEntity)
//    {
//        Log.d("TripDao", "Deleting trip: $trip")
//    }

}