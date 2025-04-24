package com.example.waygo;

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waygo.dao.TripDao
import com.example.waygo.entity.TripEntity

@Database(entities = [TripEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}