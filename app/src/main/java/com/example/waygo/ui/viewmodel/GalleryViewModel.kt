package com.example.waygo.ui.viewmodel


import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
//import com.example.waygo.data.GalleryRepository
import com.example.waygo.data.local.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GalleryViewModel(app: Application) : AndroidViewModel(app) {

//    private val repo = GalleryRepository(
//        AppDatabase.get(app).tripDao()
//    )

//    val trips = repo.trips
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//
//    init {
//        // Crea 2 viajes de demo la primera vez
//        viewModelScope.launch { repo.addDemoTripsIfEmpty() }
//    }
//
//    fun addImage(tripId: Int, uri: Uri) = viewModelScope.launch {
//        repo.addImage(tripId, uri)
//    }
//
//    fun deleteImage(uri: Uri) = viewModelScope.launch {
//        repo.deleteImageByUri(uri)
//    }
}