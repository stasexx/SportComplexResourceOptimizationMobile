package com.example.sportcomplexresourceoptimizationmobile.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ReservationCallback {
    fun onSuccess(slots: List<String>)
    fun onError(errorMessage: String)
}

class ReservationServiceImpl(private val callback: ReservationCallback) : Callback<List<String>> {
    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
        if (response.isSuccessful) {
            val slots = response.body() ?: emptyList()
            callback.onSuccess(slots)
        } else {
            val errorBody = response.errorBody()?.string()
            println("Error occurred during slots retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<List<String>>, t: Throwable) {
        callback.onError("Network error occurred during slots retrieval. Error: $t")
    }
}