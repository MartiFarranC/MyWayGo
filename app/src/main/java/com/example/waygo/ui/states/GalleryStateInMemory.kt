package com.example.waygo.ui.states

import android.net.Uri

data class Trip(
    val id: Int,
    val title: String,
    val images: MutableList<Uri> = mutableListOf()
)

