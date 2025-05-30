package com.example.waygo.data.remote.mapper

import com.example.waygo.data.remote.dto.HotelDto
import com.example.waygo.data.remote.dto.ReservationDto
import com.example.waygo.data.remote.dto.ReserveRequestDto
import com.example.waygo.data.remote.dto.RoomDto
import com.example.waygo.domain.model.Hotel
import com.example.waygo.domain.model.Reservation
import com.example.waygo.domain.model.ReserveRequest
import com.example.waygo.domain.model.Room

fun HotelDto.toDomain(): Hotel = Hotel(
    id        = id,
    name      = name,
    address   = address,
    rating    = rating,
    imageUrl  = imageUrl,
    rooms     = rooms
        ?.map { it.toDomain() }
        ?: emptyList()
)

fun RoomDto.toDomain(): Room = Room(
    id       = id,
    roomType = roomType,
    price    = price,
    images   = images
)

fun ReservationDto.toDomain(): Reservation = Reservation(
    id         = id,
    hotelId    = hotelId,
    roomId     = roomId,
    startDate  = startDate,
    endDate    = endDate,
    guestName  = guestName,
    guestEmail = guestEmail,
    hotel = hotel.toDomain(),
    room  = room.toDomain()
)

fun ReserveRequest.toDto(): ReserveRequestDto = ReserveRequestDto(
    hotelId = hotelId,
    roomId = roomId,
    startDate = startDate,
    endDate = endDate,
    guestName = guestName,
    guestEmail = guestEmail
)
