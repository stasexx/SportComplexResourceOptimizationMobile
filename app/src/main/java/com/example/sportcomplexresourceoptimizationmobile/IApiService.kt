package com.example.sportcomplexresourceoptimizationmobile

import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentRequest
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentResponse
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import com.example.sportcomplexresourceoptimizationmobile.models.RegisterModel
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationItem
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationRequest
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceRequest
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceResponse
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexRequest
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {

    @GET("users/get/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UserModel>

    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginModel>

    @POST("users/register")
    fun registerUser(@Body registerRequest: RegisterModel): Call<LoginModel>

    @GET("sportcomplexes")
    fun getSportComplexes(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<SportComplexModel>

    @PUT("sportcomplexes/update")
    fun updateSportComplex(
        @Body updateRequest: SportComplexUpdateRequest
    ): Call<Void>

    @DELETE("sportcomplexes/delete/{id}")
    fun deleteSportComplex(@Path("id") sportComplexId: String): Call<Void>

    @GET("sportcomplexes/{id}")
    fun getSportComplexDetails(@Path("id") sportComplexId: String): Call<SportComplexItem>


    @POST("sportcomplexes/create/{ownerId}")
    fun createSportComplex(@Path("ownerId") ownerId: String, @Body sportComplexRequest: SportComplexRequest): Call<SportComplexModel>

    @GET("services/visible/{sportComplexId}")
    fun getServicesForSportComplex(
        @Path("sportComplexId") sportComplexId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<ServiceResponse>

    @POST("services")
    fun createService(
        @Query("userId") userId: String,
        @Body serviceRequest: ServiceRequest
    ): Call<Void>

    @PUT("services")
    fun updateService(@Body updateRequest: ServiceUpdateRequest): Call<Void>

    @DELETE("services")
    fun deleteService(@Query("serviceId") serviceId: String): Call<Void>

    @GET("equipments/visible/{serviceId}")
    fun getEquipmentsForService(
        @Path("serviceId") serviceId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<EquipmentResponse>

    @POST("equipments/create/{serviceId}/{userId}")
    fun createEquipment(
        @Path("serviceId") serviceId: String,
        @Path("userId") userId: String,
        @Body equipmentRequest: EquipmentRequest
    ): Call<Void>

    @PUT("equipments/update")
    fun updateEquipment(@Body updateRequest: EquipmentUpdateRequest): Call<Void>

    @GET("reservations/slots/{equipmentId}/{startTime}/to/{endTime}/{duration}")
    fun getReservationSlots(
        @Path("equipmentId") equipmentId: String,
        @Path("startTime") startTime: String,
        @Path("endTime") endTime: String,
        @Path("duration") duration: Int
    ): Call<List<String>>

    @POST("reservations")
    fun createReservation(@Body reservationRequest: ReservationRequest): Call<Void>

    @GET("equipments/status/{equipmentId}")
    fun getEquipmentStatus(@Path("equipmentId") equipmentId: String): Call<Boolean>

    @GET("reservations/{userId}")
    fun getUserReservations(@Path("userId") userId: String): Call<List<ReservationItem>>

    @DELETE("equipments/delete/{equipmentId}")
    fun deleteEquipment(@Path("equipmentId") equipmentId: String): Call<Void>
}