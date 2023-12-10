package com.example.sportcomplexresourceoptimizationmobile.services

import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentItem
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface EquipmentCallback {
    fun onSuccess(equipmentList: List<EquipmentItem>)
    fun onError(errorMessage: String)
}

class EquipmentServiceImpl(private val callback: EquipmentCallback) : Callback<EquipmentResponse> {
    override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
        if (response.isSuccessful) {
            val equipmentResponse = response.body()
            val equipmentList = equipmentResponse?.items ?: emptyList()

            callback.onSuccess(equipmentList)
        } else {
            val errorBody = response.errorBody()?.string()
            callback.onError("Error occurred during equipment retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
        callback.onError("Network error occurred during equipment retrieval. Error: $t")
    }
}
