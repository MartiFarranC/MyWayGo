package com.example.waygo.dao

import androidx.room.*
import com.example.waygo.entity.ItineraryItemEntity

//TODO: Log.d...
@Dao
interface ItineraryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItinerary(item: ItineraryItemEntity)

    @Query("SELECT * FROM ItineraryItemEntity WHERE id = :id")
    suspend fun getItineraryById(id: Int): ItineraryItemEntity?

    @Query("SELECT * FROM ItineraryItemEntity")
    suspend fun getAllItineraries(): List<ItineraryItemEntity>

    @Update
    suspend fun updateItinerary(item: ItineraryItemEntity)

    @Delete
    suspend fun deleteItinerary(item: ItineraryItemEntity)
}