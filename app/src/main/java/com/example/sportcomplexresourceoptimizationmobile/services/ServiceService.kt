package com.example.sportcomplexresourceoptimizationmobile.services

import com.example.sportcomplexresourceoptimizationmobile.models.ServiceItem
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceResponse
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ServiceCallback {
    fun onSuccess(serviceList: List<ServiceItem>)
    fun onError(errorMessage: String)
}

class ServiceServiceImpl(private val callback: ServiceCallback) : Callback<ServiceResponse> {
    override fun onResponse(call: Call<ServiceResponse>, response: Response<ServiceResponse>) {
        if (response.isSuccessful) {
            val serviceResponse = response.body()
            val serviceList = serviceResponse?.items ?: emptyList()

            callback.onSuccess(serviceList)
        } else {
            val errorBody = response.errorBody()?.string()
            callback.onError("Error occurred during services retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
        callback.onError("Network error occurred during services retrieval. Error: $t")
    }
}