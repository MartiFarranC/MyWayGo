package com.example.waygo.model

data class Trip(
    val name: String,
    val destinations: String,
    val participants: String,
    val startDate: String,
    val endDate: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Trip) return false

        return name == other.name &&
                destinations == other.destinations &&
                participants == other.participants &&
                startDate == other.startDate &&
                endDate == other.endDate
    }

    override fun hashCode(): Int {
        return name.hashCode() +
                destinations.hashCode() +
                participants.hashCode() +
                startDate.hashCode() +
                endDate.hashCode()
    }
}