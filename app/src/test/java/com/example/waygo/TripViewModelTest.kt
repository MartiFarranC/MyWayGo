package com.example.waygo

import com.example.waygo.model.Trip
import com.example.waygo.viewmodel.TripViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TripViewModelTest {

    private lateinit var tripViewModel: TripViewModel

    @Before
    fun setup() {
        // Initialize the ViewModel with a mock or fake repository
        tripViewModel = TripViewModel()
    }

    @Test
    fun `test add trip`() = runBlocking {
        val trip = Trip(
            name = "Test Trip",
            destinations = "Paris, London",
            participants = "Alice, Bob",
            startDate = "01/01/2024",
            endDate = "10/01/2024"
        )

        tripViewModel.addTrip(trip)
        val trips = tripViewModel.trips.first()

        assertTrue(trips.contains(trip))
    }

    @Test
    fun `test get trips`() = runBlocking {
        val trip1 = Trip("Trip 1", "Paris", "Alice", "01/01/2024", "05/01/2024")
        val trip2 = Trip("Trip 2", "London", "Bob", "06/01/2024", "10/01/2024")

        tripViewModel.addTrip(trip1)
        tripViewModel.addTrip(trip2)

        val trips = tripViewModel.trips.first()

        assertEquals(2, trips.size)
        assertTrue(trips.contains(trip1))
        assertTrue(trips.contains(trip2))
    }

    @Test
    fun `test update trip`() = runBlocking {
        val trip = Trip("Trip 1", "Paris", "Alice", "01/01/2024", "05/01/2024")
        tripViewModel.addTrip(trip)

        val updatedTrip = trip.copy(name = "Trip 1", destinations = "London")
        tripViewModel.editTrip(updatedTrip)

        val trips = tripViewModel.trips.first()
        println("Trips after update: $trips")

        assertTrue(trips.contains(updatedTrip))
        assertFalse(trips.contains(trip))
    }

    @Test
    fun `test delete trip`() = runBlocking {
        val trip = Trip("Trip 1", "Paris", "Alice", "01/01/2024", "05/01/2024")
        tripViewModel.addTrip(trip)

        tripViewModel.deleteTrip(trip)
        val trips = tripViewModel.trips.first()

        assertFalse(trips.contains(trip))
    }
}