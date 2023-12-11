package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexRequest
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.SportComplexItemService
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback

class CreateSportComplexActivity : AppCompatActivity() {

    private val apiService = ApiServiceImpl()

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var operatingHoursEditText: EditText
    private lateinit var createButton: Button
    private lateinit var updateButton: Button

    private var isUpdate = false
    private var sportComplexId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sport_complex)

        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        cityEditText = findViewById(R.id.editTextCity)
        addressEditText = findViewById(R.id.editTextAddress)
        descriptionEditText = findViewById(R.id.editTextDescription)
        operatingHoursEditText = findViewById(R.id.editTextOperatingHours)
        createButton = findViewById(R.id.buttonCreate)
        updateButton = findViewById(R.id.buttonUpdate)

        // Перевіряємо, чи передано дані для оновлення
        if (intent.hasExtra("SPORT_COMPLEX_ID")) {
            isUpdate = true
            sportComplexId = intent.getStringExtra("SPORT_COMPLEX_ID")
            // Отримуємо дані спорткомплексу для оновлення і заповнюємо поля
            fetchSportComplexDetails()
        }

        createButton.setOnClickListener {
            createSportComplex()
        }

        updateButton.setOnClickListener {
            updateSportComplex()
        }
    }

    private fun createSportComplex() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val city = cityEditText.text.toString()
        val address = addressEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val operatingHours = operatingHoursEditText.text.toString()

        val sportComplexRequest = SportComplexRequest(name, email, city, address, description, operatingHours)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)
        println(userEmail)
        if (userEmail != null) {
            apiService.getUserByEmail(userEmail, object : UserCallback {
                override fun onSuccess(user: UserModel) {
                    val userId = user.id

                    // Використовуйте userId при виклику createSportComplex
                    apiService.createSportComplex(userId, sportComplexRequest, object : ApiServiceImpl.ApiCallback {
                        override fun onSuccess(result: String) {
                            val intent = Intent(this@CreateSportComplexActivity, SportComplexActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onError(error: String) {
                            // Обробте помилку, якщо потрібно
                        }
                    })
                }

                override fun onError(error: String) {
                    // Обробте помилку, якщо потрібно
                }
            })
        }
    }

    private fun updateSportComplex() {
        println("ЗАЙШОВ В АПДЕЙТ")
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val city = cityEditText.text.toString()
        val address = addressEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val operatingHours = operatingHoursEditText.text.toString()

        // Створіть об'єкт SportComplexUpdateRequest
        val updateRequest = SportComplexUpdateRequest(sportComplexId ?: "", name, email, city, address, description, operatingHours, 0.0)
        println("АЙДІШНІК " + sportComplexId)
        // Відправте PUT-запит
        if (sportComplexId != null) {
            apiService.updateSportComplex( updateRequest, object : ApiServiceImpl.ApiCallback {
                override fun onSuccess(result: String) {
                    println("АЙДІШНІК " + sportComplexId)
                    val intent = Intent(this@CreateSportComplexActivity, SportComplexActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onError(error: String) {
                    println("ПОМИЛКА " + error)
                }
            })
        }
    }

    private fun fetchSportComplexDetails() {
        if (sportComplexId != null) {
            apiService.getSportComplexDetails(sportComplexId!!, object : SportComplexItemService {
                override fun onSuccess(sportComplex: SportComplexItem) {
                    // Заповніть поля на основі отриманих даних
                    nameEditText.setText(sportComplex.name)
                    emailEditText.setText(sportComplex.email)
                    cityEditText.setText(sportComplex.city)
                    addressEditText.setText(sportComplex.address)
                    descriptionEditText.setText(sportComplex.description)
                    operatingHoursEditText.setText(sportComplex.operatingHours)
                }

                override fun onError(error: String) {
                    // Обробте помилку, якщо потрібно
                    // Наприклад, можливо, виведення повідомлення про помилку
                    // або виклик інших методів обробки помилок
                }
            })
        }
    }

}