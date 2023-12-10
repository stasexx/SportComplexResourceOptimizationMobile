package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationRequest
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.ReservationCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback
import java.text.SimpleDateFormat
import java.util.*

class ReservationActivity : AppCompatActivity() {

    private lateinit var durationSpinner: Spinner
    private lateinit var loadInfoButton: Button
    private lateinit var timeSlotSpinner: Spinner
    private lateinit var intervalSpinner: Spinner
    private lateinit var createReservationButton: Button
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        durationSpinner = findViewById(R.id.durationSpinner)
        intervalSpinner = findViewById(R.id.intervalSpinner)
        loadInfoButton = findViewById(R.id.loadInfoButton)
        timeSlotSpinner = findViewById(R.id.timeSlotSpinner)
        createReservationButton = findViewById(R.id.createReservationButton)

        // Налаштування адаптера для спінера тривалості
        val durationAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.duration_options,
            android.R.layout.simple_spinner_item
        )
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        durationSpinner.adapter = durationAdapter

        // Налаштування адаптера для спінера для обрання часового слоту
        val timeSlotAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.time_slots,
            android.R.layout.simple_spinner_item
        )
        timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSlotSpinner.adapter = timeSlotAdapter

        // Налаштування адаптера для спінера для обрання інтервалу
        val intervalAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.intervals,
            android.R.layout.simple_spinner_item
        )
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        intervalSpinner.adapter = intervalAdapter

        val equipmentId = intent.getStringExtra("EQUIMENT_ID") ?: ""
        // Обробник натискання кнопки завантаження інформації
        loadInfoButton.setOnClickListener {
            val duration = durationSpinner.selectedItem.toString().toInt()
            val selectedInterval = intervalSpinner.selectedItem.toString()

            // Розділити інтервал часу на початковий та кінцевий час
            val intervalParts = selectedInterval.split(" - ")
            val startTimeString = intervalParts[0]
            val endTimeString = intervalParts[1]

            // Перевести початковий та кінцевий час у формат UTC
            val startTimeUTC = convertToNormalTime(startTimeString)
            val endTimeUTC = convertToNormalTime(endTimeString)
            println(startTimeUTC)
            println(endTimeUTC)

            apiService.getAvailableSlots("65550191df174c2e4c781bab", startTimeUTC, endTimeUTC, duration, object : ReservationCallback {
                override fun onSuccess(result: List<String>) {
                    val timeSlotAdapter = ArrayAdapter<String>(
                        this@ReservationActivity,
                        android.R.layout.simple_spinner_item,
                        result
                    )
                    timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    timeSlotSpinner.adapter = timeSlotAdapter
                }

                override fun onError(error: String) {
                    // Обробка помилки, якщо потрібно
                }
            })
        }

        // Обробник натискання кнопки створення резервації
        createReservationButton.setOnClickListener {
            val duration = durationSpinner.selectedItem.toString().toInt()
            val selectedInterval = timeSlotSpinner.selectedItem.toString()

            // Розділити інтервал часу на початковий та кінцевий час
            val intervalParts = selectedInterval.split("-")
            val startTimeString = intervalParts[0]
            val endTimeString = intervalParts[1]

            // Перевести початковий та кінцевий час у формат UTC
            val startTimeUTC = convertToNormalTime(startTimeString)
            val endTimeUTC = convertToNormalTime(endTimeString)
            println(startTimeUTC)
            println(endTimeUTC)

            // Отримати ID користувача (вам потрібно замінити "user@gmail.com" на актуальний email користувача)
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("email", null)
            println(userEmail)
            if (userEmail != null) {
                apiService.getUserByEmail(userEmail, object : UserCallback {
                    override fun onSuccess(user: UserModel) {
                        val userId = user.id
                        // Викликати метод для створення резервації
                        val reservationRequest = ReservationRequest(
                            startReservation = startTimeUTC,
                            duration = duration,
                            endReservation = endTimeUTC,
                            equipmentId = "65550191df174c2e4c781bab",
                            userId = userId
                        )
                        apiService.createReservation(reservationRequest, object :
                            ApiServiceImpl.ApiCallback {
                            override fun onSuccess(result: String) {
                                // Оновити інтерфейс або виконати інші дії при успішному створенні резервації
                            }

                            override fun onError(error: String) {
                                // Обробити помилку при створенні резервації
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
    private fun convertToNormalTime(timeString: String): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(currentTime)

        val combinedDateTimeString = "$currentDate $timeString"

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = format.parse(combinedDateTimeString)

        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).format(date)
    }
}