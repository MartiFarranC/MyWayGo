package com.example.waygo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.waygo.entity.ItineraryItemEntity
import com.example.waygo.entity.TripEntity
import com.example.waygo.dao.TripDao
import com.example.waygo.dao.ItineraryItemDao

@Database(
    entities = [TripEntity::class, ItineraryItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryItemDao(): ItineraryItemDao
}