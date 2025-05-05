package com.example.waygo

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.waygo.dao.TripDao
import com.example.waygo.database.AppDatabase
import com.example.waygo.entity.TripEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TripDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var tripDao: TripDao

    @Before
    fun setup() {
        // Create an in-memory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        tripDao = database.tripDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testAddAndRetrieveTrip() = runBlocking {
        val trip = TripEntity(
            id = 1,
            name = "Test Trip",
            destinations = "Paris, London",
            participants = "Alice, Bob",
            startDate = "01/01/2024",
            endDate = "10/01/2024"
        )

        tripDao.addTrip(trip)
        val trips = tripDao.getAllTrips()

        assertEquals(1, trips.size)
        assertEquals(trip, trips[0])
    }

    @Test
    fun testDeleteTrip() = runBlocking {
        val trip = TripEntity(
            id = 1,
            name = "Test Trip",
            destinations = "Paris, London",
            participants = "Alice, Bob",
            startDate = "01/01/2024",
            endDate = "10/01/2024"
        )

        tripDao.deleteTrip(trip)
        val trips = tripDao.getAllTrips()

        assertTrue(trips.isEmpty())
    }

    @Test
    fun testUpdateTrip() = runBlocking {
        var trip = TripEntity(
            id = 1,
            name = "Test Trip",
            destinations = "Paris, London",
            participants = "Alice, Bob",
            startDate = "01/01/2024",
            endDate = "10/01/2024"
        )

        tripDao.addTrip(trip)

        trip = TripEntity(
            id = 1,
            name = "Test Trip2",
            destinations = "Paris, London, Berlin",
            participants = "Alice",
            startDate = "02/01/2024",
            endDate = "11/01/2024"
        )

        tripDao.updateTrip(trip)
        val trips = tripDao.getAllTrips()

        assertEquals(trip, trips[0])
    }
}