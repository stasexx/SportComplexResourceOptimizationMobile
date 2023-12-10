package com.example.sportcomplexresourceoptimizationmobile

import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentResponse
import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import com.example.sportcomplexresourceoptimizationmobile.models.RegisterModel
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceResponse
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {
    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginModel>

    @POST("users/register")
    fun registerUser(@Body registerRequest: RegisterModel): Call<LoginModel>

    @GET("sportcomplexes")
    fun getSportComplexes(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<SportComplexModel>

    @GET("services/visible/{sportComplexId}")
    fun getServicesForSportComplex(
        @Path("sportComplexId") sportComplexId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<ServiceResponse>

    @GET("equipments/visible/{serviceId}")
    fun getEquipmentsForService(
        @Path("serviceId") serviceId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<EquipmentResponse>

    @GET("reservations/slots/{equipmentId}/{startTime}/to/{endTime}/{duration}")
    fun getReservationSlots(
        @Path("equipmentId") equipmentId: String,
        @Path("startTime") startTime: String,
        @Path("endTime") endTime: String,
        @Path("duration") duration: Int
    ): Call<List<String>>
}