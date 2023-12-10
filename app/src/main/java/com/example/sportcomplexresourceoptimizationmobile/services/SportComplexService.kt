package com.example.sportcomplexresourceoptimizationmobile.services

import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SportComplexService {
    fun onSuccess(sportComplexList: List<SportComplexItem>)
    fun onError(errorMessage: String)
}

// Оновлений клас SportComplexServiceImpl
class SportComplexServiceImpl(private val callback: SportComplexService) :
    Callback<SportComplexModel> {

    override fun onResponse(
        call: Call<SportComplexModel>,
        response: Response<SportComplexModel>
    ) {
        if (response.isSuccessful) {
            val sportComplexModel = response.body()
            val sportComplexList = sportComplexModel?.items ?: emptyList()

            callback.onSuccess(sportComplexList)
        } else {
            val errorBody = response.errorBody()?.string()
            callback.onError("Error occurred during sport complexes retrieval. Code: ${response.code()}, Body: $errorBody")
        }
    }

    override fun onFailure(call: Call<SportComplexModel>, t: Throwable) {
        callback.onError("Network error occurred during sport complexes retrieval. Error: $t")
    }
}