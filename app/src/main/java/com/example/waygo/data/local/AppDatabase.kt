package com.example.waygo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waygo.data.local.dao.SubTaskDao
import com.example.waygo.data.local.dao.TaskDao
import com.example.waygo.data.local.entity.SubTaskEntity
import com.example.waygo.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, SubTaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
}