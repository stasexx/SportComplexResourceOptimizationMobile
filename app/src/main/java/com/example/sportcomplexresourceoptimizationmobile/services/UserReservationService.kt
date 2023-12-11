package com.example.sportcomplexresourceoptimizationmobile.services

import com.example.sportcomplexresourceoptimizationmobile.models.ReservationItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserReservationCallback {
    fun onSuccess(reservations: List<ReservationItem>)
    fun onError(errorMessage: String)
}

class UserReservationServiceImpl(private val callback: UserReservationCallback) : Callback<List<ReservationItem>> {
    override fun onResponse(call: Call<List<ReservationItem>>, response: Response<List<ReservationItem>>) {
        if (response.isSuccessful) {
            val reservations = response.body() ?: emptyList()
            callback.onSuccess(reservations)
        } else {
            val errorBody = response.errorBody()?.string()
            callback.onError("Error occurred during reservations retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<List<ReservationItem>>, t: Throwable) {
        callback.onError("Network error occurred during reservations retrieval. Error: $t")
    }
}
