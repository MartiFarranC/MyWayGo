package com.example.waygo.domain.model

import android.net.Uri

data class Trip(
    val id: Int = 0,
    val name: String,
    val destinations: String,
    val participants: String,
    val startDate: String,
    val endDate: String,
    val userId: String,
    val images: List<Uri>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Trip) return false

        return id == other.id &&
                name == other.name &&
                destinations == other.destinations &&
                participants == other.participants &&
                startDate == other.startDate &&
                endDate == other.endDate &&
                userId == other.userId &&
                images == other.images
    }

    override fun hashCode(): Int {
        return id.hashCode() +
                name.hashCode() +
                destinations.hashCode() +
                participants.hashCode() +
                startDate.hashCode() +
                endDate.hashCode() +
                userId.hashCode()+
                images.hashCode()
    }
}