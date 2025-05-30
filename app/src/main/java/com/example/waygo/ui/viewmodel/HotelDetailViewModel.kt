package com.example.waygo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.domain.model.Hotel
import com.example.waygo.domain.model.ReserveRequest
import com.example.waygo.domain.model.Room
import com.example.waygo.domain.repository.HotelRepository
import com.example.waygo.utils.ErrorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HotelDetailViewModel @Inject constructor(
    private val repo: HotelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HotelDetailUiState())
    val uiState: StateFlow<HotelDetailUiState> = _uiState

    fun selectRoom(room: Room) {
        _uiState.value = _uiState.value.copy(selectedRoom = room)
    }

    private lateinit var groupId: String
    private lateinit var start: String
    private lateinit var end: String

    /* -------- load hotel & free rooms -------- */
    fun load(hotelId: String, gid: String, s: String, e: String) {
        if (uiState.value.hotel != null) return   // already loaded
        groupId = gid; start = s; end = e
        viewModelScope.launch {
            val hotel = repo.getHotels(gid).first { it.id == hotelId }
            val freeRooms = repo.getAvailability(gid, s, e)
                .first { it.id == hotelId }.rooms
            _uiState.value = HotelDetailUiState(false, hotel, freeRooms)
        }
    }

    /* -------- reserve selected room -------- */
    fun reserveRoom(room: Room) = viewModelScope.launch {

        val req = ReserveRequest(
            hotelId = uiState.value.hotel!!.id,
            roomId  = room.id,
            startDate = start,
            endDate   = end,
            guestName = "User",
            guestEmail = "user@gmail.com"
        )

        try {
            repo.reserve(groupId, req)
        } catch (e: HttpException) {
            val decodedError = ErrorUtils.extractErrorMessage(e)
            Log.e("BookViewModel", "HTTP error: ${decodedError}  $e")

        } catch (e: Exception) {
            Log.e("BookViewModel", "Error: ${e.localizedMessage}")
        }

    }
}


data class HotelDetailUiState(
    val loading: Boolean = true,
    val hotel: Hotel? = null,
    val rooms: List<Room>? = emptyList(),
    val selectedRoom: Room? = null,
    val showImageDialog: Boolean = false,
)