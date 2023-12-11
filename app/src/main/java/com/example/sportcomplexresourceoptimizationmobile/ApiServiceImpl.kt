package com.example.sportcomplexresourceoptimizationmobile

import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import com.example.sportcomplexresourceoptimizationmobile.models.RegisterModel
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationRequest
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexRequest
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.services.EquipmentCallback
import com.example.sportcomplexresourceoptimizationmobile.services.EquipmentServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.services.ReservationCallback
import com.example.sportcomplexresourceoptimizationmobile.services.ReservationServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.services.ServiceCallback
import com.example.sportcomplexresourceoptimizationmobile.services.ServiceServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.services.SportComplexItemService
import com.example.sportcomplexresourceoptimizationmobile.services.SportComplexService
import com.example.sportcomplexresourceoptimizationmobile.services.SportComplexServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserReservationCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserReservationServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.services.UserServiceImpl
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

    fun getSportComplexes(pageNumber: Int, pageSize: Int, callback: SportComplexService) {
        val call = apiService.getSportComplexes(pageNumber, pageSize)
        println("я тута")
        println(call)

        call.enqueue(SportComplexServiceImpl(callback))
    }

    fun getServicesForSportComplex(sportComplexId: String, pageNumber: Int, pageSize: Int, callback: ServiceCallback) {
        val call = apiService.getServicesForSportComplex(sportComplexId, pageNumber, pageSize)

        call.enqueue(ServiceServiceImpl(callback))
    }

    fun getEquipmentsForSportComplex(serviceId: String, pageNumber: Int, pageSize: Int, callback: EquipmentCallback) {
        val call = apiService.getEquipmentsForService(serviceId, pageNumber, pageSize)

        call.enqueue(EquipmentServiceImpl(callback))
    }

    fun getAvailableSlots(
        equipmentId: String,
        startTime: String,
        endTime: String,
        duration: Int,
        callback: ReservationCallback
    ) {
        val call = apiService.getReservationSlots(equipmentId, startTime, endTime, duration)
        println(call)
        call.enqueue(ReservationServiceImpl(callback))
    }

    fun getUserByEmail(email: String, callback: UserCallback) {
        val call = apiService.getUserByEmail(email)
        call.enqueue(UserServiceImpl(callback))
    }

    fun createReservation(reservationRequest: ReservationRequest, callback: ApiCallback) {
        val call = apiService.createReservation(reservationRequest)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    println("Reservation created successfully.")
                } else {
                    println("Error occurred during reservation creation.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Network error occurred during reservation creation.")
            }
        })
    }

    fun getEquipmentStatus(equipmentId: String, callback: ApiCallback) {
        val call = apiService.getEquipmentStatus(equipmentId)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                println(call)
                if (response.isSuccessful) {
                    println("УСПІШНО")
                    val status = response.body() ?: false
                    println(status)
                    callback.onSuccess(status.toString())
                } else {
                    println("Error occurred during equipment status retrieval.")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                println("Network error occurred during equipment status retrieval. Error: $t")
            }
        })
    }

    fun createSportComplex(ownerId: String, sportComplexRequest: SportComplexRequest, callback: ApiCallback) {
        val call = apiService.createSportComplex(ownerId, sportComplexRequest)
        println("CALL " + call)
        call.enqueue(object : Callback<SportComplexModel> {
            override fun onResponse(call: Call<SportComplexModel>, response: Response<SportComplexModel>) {
                if (response.isSuccessful) {
                    val sportComplex = response.body()
                    if (sportComplex != null) {
                        callback.onSuccess(sportComplex.toString())
                    } else {
                        callback.onError("Failed to create sport complex")
                    }
                } else {
                    callback.onError("Failed to create sport complex")
                }
            }

            override fun onFailure(call: Call<SportComplexModel>, t: Throwable) {
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }

    fun updateSportComplex(updateRequest: SportComplexUpdateRequest, callback: ApiCallback) {
        val call = apiService.updateSportComplex( updateRequest)
        println("CALL " + call)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println("RESPONSE " + response)
                if (response.isSuccessful) {
                    callback.onSuccess("Sport complex updated successfully.")
                } else {
                    callback.onError("Error occurred during sport complex update.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Network error occurred during sport complex update.")
            }
        })
    }

    fun getSportComplexDetails(sportComplexId: String, callback: SportComplexItemService) {
        val call = apiService.getSportComplexDetails(sportComplexId)
        call.enqueue(object : Callback<SportComplexItem> {
            override fun onResponse(call: Call<SportComplexItem>, response: Response<SportComplexItem>) {
                if (response.isSuccessful) {
                    val sportComplex = response.body()
                    if (sportComplex != null) {
                        callback.onSuccess(sportComplex)
                    } else {
                        callback.onError("Failed to fetch sport complex details")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    callback.onError("Error occurred during sport complex details retrieval. Code: ${response.code()}, Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<SportComplexItem>, t: Throwable) {
                callback.onError("Network error occurred during sport complex details retrieval. Error: $t")
            }
        })
    }

    fun deleteSportComplex(sportComplexId: String, callback: ApiCallback) {
        val call = apiService.deleteSportComplex(sportComplexId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess("Sport complex deleted successfully.")
                } else {
                    callback.onError("Error occurred during sport complex deletion.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Network error occurred during sport complex deletion.")
            }
        })
    }


    fun getUserReservations(userId: String, callback: UserReservationCallback) {
        val call = apiService.getUserReservations(userId)
        call.enqueue(UserReservationServiceImpl(callback))
    }
}