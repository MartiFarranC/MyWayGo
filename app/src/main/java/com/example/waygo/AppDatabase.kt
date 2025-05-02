package com.example.waygo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.waygo.entity.ItineraryItemEntity
import com.example.waygo.entity.TripEntity
import com.example.waygo.dao.TripDao
import com.example.waygo.dao.ItineraryItemDao
import com.example.waygo.model.Trip

@Database(
    entities = [TripEntity::class, ItineraryItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryItemDao(): ItineraryItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "waygo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}