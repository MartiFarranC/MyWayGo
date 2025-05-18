package com.example.waygo.ui.states

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Trip(
    val id: Int,
    val title: String,
    val images: MutableList<Uri> = mutableListOf()
)

class GalleryState {
    private val _trips = MutableStateFlow(
        listOf(
            Trip(1, "Paris (30-May -> 06-June)"), //TODO
            Trip(2, "London (10-June -> 15-June)")
        )
    )
    val trips = _trips.asStateFlow()

    fun addImage(tripId: Int, uri: Uri) {
        _trips.value = _trips.value.map { trip ->
            if (trip.id == tripId)
                trip.copy(images = (trip.images + uri).toMutableList())
            else trip
        }
    }
}