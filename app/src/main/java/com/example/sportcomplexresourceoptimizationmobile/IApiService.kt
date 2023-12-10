package com.example.sportcomplexresourceoptimizationmobile

import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import com.example.sportcomplexresourceoptimizationmobile.models.RegisterModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IApiService {
    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginModel>

    @POST("users/register")
    fun registerUser(@Body registerRequest: RegisterModel): Call<LoginModel>
}