package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.adapters.UserReservationAdapter
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationItem
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.ReservationCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserReservationCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UsersReservationsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservationAdapter: UserReservationAdapter
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_reservations)

        recyclerView = findViewById(R.id.recyclerViewUserReservations)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reservationAdapter = UserReservationAdapter(emptyList())
        recyclerView.adapter = reservationAdapter

        val toggleTimeFormatButton: Button = findViewById(R.id.toggleTimeFormatButton)
        toggleTimeFormatButton.setOnClickListener {
            reservationAdapter.toggleTimeFormat()
        }

        fetchReservations()

    }

    private fun fetchReservations() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)
        println(userEmail)
        if (userEmail != null) {
            apiService.getUserByEmail(userEmail, object : UserCallback {
                override fun onSuccess(user: UserModel) {
                    val userId = user.id
                    apiService.getUserReservations(userId, object : UserReservationCallback {
                        override fun onSuccess(result: List<ReservationItem>) {
                            // Парсінг отриманих даних та оновлення адаптера
                            val reservations = result
                            reservationAdapter.updateData(reservations)
                        }

                        override fun onError(error: String) {
                            // Обробка помилок, якщо потрібно
                        }
                    })
                }

                override fun onError(errorMessage: String) {
                    // Обробити помилку при отриманні користувача
                }
            })
        }
    }
}
