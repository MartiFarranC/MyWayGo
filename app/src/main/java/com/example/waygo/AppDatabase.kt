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
import com.example.waygo.dao.UserDao
import com.example.waygo.entity.UserEntity
import com.example.waygo.model.Trip
import com.example.waygo.util.DateConverter


@Database(
    entities = [TripEntity::class, ItineraryItemEntity::class, UserEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun itineraryItemDao(): ItineraryItemDao
    abstract fun userDao(): UserDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "waygo_database"
                )
                    .fallbackToDestructiveMigration() // Allows destructive migration TODO: ??
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}