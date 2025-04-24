package com.example.waygo.dao

import androidx.room.*
import com.example.waygo.entity.ItineraryItemEntity

@Dao
interface ItineraryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItem(item: ItineraryItemEntity)

    @Update
    suspend fun updateItineraryItem(item: ItineraryItemEntity)

    @Delete
    suspend fun deleteItineraryItem(item: ItineraryItemEntity)

    @Query("SELECT * FROM itinerary_items WHERE id = :id")
    suspend fun getItineraryItemById(id: Int): ItineraryItemEntity?

    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId")
    suspend fun getItineraryItemsByTripId(tripId: Int): List<ItineraryItemEntity>
}