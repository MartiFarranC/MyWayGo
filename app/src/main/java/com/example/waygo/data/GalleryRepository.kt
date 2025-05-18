package com.example.waygo.data

import android.net.Uri
import com.example.waygo.ui.states.Trip
import com.example.waygo.data.local.dao.TripDao
import com.example.waygo.data.local.entity.ImageEntity
import com.example.waygo.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File

//class GalleryRepository(private val dao: TripDao) {
//
//    /** Expone los datos ya convertidos a modelo de UI */
//    val trips: Flow<List<Trip>> = dao.getTripsWithImages()
//        .map { list -> list.map { it.toTrip() } }
//
//    suspend fun addDemoTripsIfEmpty() {
//        if (dao.getTripsWithImages().first().isEmpty()) {
//            dao.insertTrip(TripEntity(title = "Paris getaway"))
//            dao.insertTrip(TripEntity(title = "Coast road trip"))
//        }
//    }
//
//    suspend fun addImage(tripId: Int, uri: Uri) {
//        dao.insertImage(ImageEntity(tripId = tripId, uri = uri.toString()))
//    }
//
//    /* ---------- mapping helpers ---------- */
//
//    private fun TripWithImages.toTrip() = Trip(
//        id = trip.id,
//        title = trip.title,
//        images = images.map { Uri.parse(it.uri) }.toMutableList()
//    )
//
//    suspend fun deleteImageByUri(uri: Uri) {
//        dao.deleteImageByUri(uri.toString())      // Room
//        File(uri.path!!).delete()                 // physical file
//    }
//}