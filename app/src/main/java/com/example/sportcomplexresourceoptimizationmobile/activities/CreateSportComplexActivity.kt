package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexRequest
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
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

        createButton.setOnClickListener {
            createSportComplex()
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
}