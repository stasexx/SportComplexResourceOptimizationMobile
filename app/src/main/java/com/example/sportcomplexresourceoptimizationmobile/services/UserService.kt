package com.example.sportcomplexresourceoptimizationmobile.services

import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserCallback {
    fun onSuccess(user: UserModel)
    fun onError(errorMessage: String)
}

class UserServiceImpl(private val callback: UserCallback) : Callback<UserModel> {
    override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
        if (response.isSuccessful) {
            val user = response.body()
            if (user != null) {
                callback.onSuccess(user)
            } else {
                callback.onError("Empty response body")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            callback.onError("Error occurred during user retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<UserModel>, t: Throwable) {
        callback.onError("Network error occurred during user retrieval. Error: $t")
    }
}