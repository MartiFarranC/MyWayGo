package com.example.waygo.repository

import com.example.waygo.dao.ItineraryItemDao
import com.example.waygo.entity.ItineraryItemEntity

class ItineraryRepository(private val ItineraryItemDao: ItineraryItemDao) {

    suspend fun addItinerary(itinerary: ItineraryItemEntity) {
        ItineraryItemDao.addItinerary(itinerary)
    }

    suspend fun getItineraryById(id: Int): ItineraryItemEntity? {
        return ItineraryItemDao.getItineraryById(id)
    }

    suspend fun getAllItineraries(): List<ItineraryItemEntity> {
        return ItineraryItemDao.getAllItineraries()
    }

    suspend fun updateItinerary(itinerary: ItineraryItemEntity) {
        ItineraryItemDao.updateItinerary(itinerary)
    }

    suspend fun deleteItinerary(itinerary: ItineraryItemEntity) {
        ItineraryItemDao.deleteItinerary(itinerary)
    }
}