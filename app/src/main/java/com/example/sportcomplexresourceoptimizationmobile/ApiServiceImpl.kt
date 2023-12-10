package com.example.sportcomplexresourceoptimizationmobile

import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import com.example.sportcomplexresourceoptimizationmobile.models.RegisterModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceImpl {

    interface ApiCallback {
        fun onSuccess(result: String)
        fun onError(error: String)
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.50.140:5002/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    private val apiService = retrofit.create(IApiService::class.java)

    fun loginUser(email: String, password: String, callback: ApiCallback) {
        val loginRequest = LoginRequest(email, password)
        makeApiRequest(loginRequest, callback)
    }

    fun registerUser(registerModel: RegisterModel, callback: ApiCallback) {
        val call = apiService.registerUser(registerModel)

        call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.toString() ?: "")
                } else {
                    callback.onError("Error occurred during registration.")
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                callback.onError("Network error occurred during registration.")
            }
        })
    }

    fun makeApiRequest(loginRequest: LoginRequest, callback: ApiCallback) {
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.isSuccessful) {
                    println("УСПІШНО")
                    callback.onSuccess(response.body()?.toString() ?: "")
                } else {
                    callback.onError("Error occurred during API request.")
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                callback.onError("Network error occurred.")
            }
        })
    }
}