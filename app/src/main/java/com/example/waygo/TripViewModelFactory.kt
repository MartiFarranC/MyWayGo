package com.example.waygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.waygo.dao.TripDao

class TripViewModelFactory(private val tripDao: TripDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(tripDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}