package com.example.sportcomplexresourceoptimizationmobile.services

import retrofit2.Call
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import retrofit2.Response
import retrofit2.Callback

interface SportComplexItemService {
    fun onSuccess(sportComplexItem: SportComplexItem)
    fun onError(errorMessage: String)
}

// Оновлений клас SportComplexItemServiceImpl
class SportComplexItemServiceImpl(private val callback: SportComplexItemService) :
    Callback<SportComplexItem> {

    override fun onResponse(
        call: Call<SportComplexItem>,
        response: Response<SportComplexItem>
    ) {
        if (response.isSuccessful) {
            val sportComplexModel = response.body()
            val sportComplexList = sportComplexModel

            val selectedSportComplex = sportComplexList
            if (selectedSportComplex != null) {
                callback.onSuccess(selectedSportComplex)
            }
        } else {
            val errorBody = response.errorBody()?.string()
            callback.onError("Error occurred during sport complexes retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<SportComplexItem>, t: Throwable) {
        callback.onError("Network error occurred during sport complexes retrieval. Error: $t")
    }
}