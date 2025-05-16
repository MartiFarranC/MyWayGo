package com.example.waygo.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.waygo.domain.model.Hotel
import com.example.waygo.domain.repository.HotelRepository
import java.time.LocalDate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.BuildConfig
import com.example.waygo.utils.ErrorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import retrofit2.HttpException



@HiltViewModel
class BookViewModel @Inject constructor(
    private val repo: HotelRepository
) : ViewModel() {

    val groupId = BuildConfig.GROUP_ID

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState

    /* ---------- city picker ---------- */
    fun toggleCityMenu() = _uiState.update { it.copy(cityMenu = !it.cityMenu) }
    fun selectCity(c: String) = _uiState.update { it.copy(city = c, cityMenu = false) }

    /* ---------- date pickers ---------- */
    fun pickStart(d: java.time.LocalDate) = _uiState.update { it.copy(startDate = d) }
    fun pickEnd(d: java.time.LocalDate)   = _uiState.update { it.copy(endDate = d) }

    /* ---------- search ---------- */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun search() = viewModelScope.launch {
        val s = _uiState.value.startDate ?: return@launch
        val e = _uiState.value.endDate   ?: return@launch
        val fmt = DateTimeFormatter.ISO_DATE
        val city = _uiState.value.city

        _uiState.update { it.copy(loading = true, message = null) }

        try {
            val hotels = repo.getAvailability(groupId, s.format(fmt), e.format(fmt), city = city)
            _uiState.update { it.copy(loading = false, hotels = hotels) }
        } catch (e: HttpException) {

            val decodedError = ErrorUtils.extractErrorMessage(e)

            Log.e("BookViewModel", "HTTP error: ${decodedError}  $e")
            _uiState.update { it.copy(loading = false, hotels = emptyList(), message = decodedError) }

            _uiState.update {
                it.copy(
                    loading = false,
                    hotels = emptyList(),
                    message = "Error: ${decodedError}}"
                )
            }

        } catch (e: Exception) {
            Log.e("BookViewModel", "Error: ${e.localizedMessage}")
//            _uiState.update { it.copy(loading = false, hotels = emptyList()) }
            _uiState.update {
                it.copy(
                    loading = false,
                    hotels = emptyList(),
                    message = "Error: ${e.message}}"
                )
            }
        }
    }

}


data class BookUiState(
    val loading: Boolean = false,
    val cityMenu: Boolean = false,
    val city: String = "Barcelona",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val hotels: List<Hotel> = emptyList(),
    val message: String? = null
)