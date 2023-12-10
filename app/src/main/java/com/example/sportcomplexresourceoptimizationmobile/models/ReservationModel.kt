package com.example.sportcomplexresourceoptimizationmobile.models

data class ReservationRequest(
    val startReservation: String,
    val duration: Int,
    val endReservation: String,
    val equipmentId: String,
    val userId: String
)