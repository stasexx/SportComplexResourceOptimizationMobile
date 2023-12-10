package com.example.sportcomplexresourceoptimizationmobile

import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IApiService {
    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginModel>
}