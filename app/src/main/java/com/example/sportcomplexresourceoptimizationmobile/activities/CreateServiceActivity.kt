package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceRequest
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback

class CreateServiceActivity : AppCompatActivity() {

    private val apiService = ApiServiceImpl()

    private lateinit var serviceNameEditText: EditText
    private lateinit var createButton: Button

    private var isUpdate = false
    private var serviceIdToUpdate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_service)

        serviceNameEditText = findViewById(R.id.editTextServiceName)
        createButton = findViewById(R.id.buttonCreateService)

        // Отримати інформацію про оновлення, якщо це необхідно
        val serviceNameToUpdate = intent.getStringExtra("SERVICE_NAME")
        serviceIdToUpdate = intent.getStringExtra("SERVICE_ID")

        if (serviceNameToUpdate != null) {
            isUpdate = true
            serviceNameEditText.setText(serviceNameToUpdate)
        }

        createButton.setOnClickListener {
            if (isUpdate) {
                updateService()
            } else {
                createService()
            }
        }
    }

    private fun createService() {
        val serviceName = serviceNameEditText.text.toString()
        val sportComplexId = intent.getStringExtra("SPORT_COMPLEX_ID") ?: ""

        val serviceRequest = ServiceRequest(serviceName, sportComplexId)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)


        if (userEmail != null) {
            apiService.getUserByEmail(userEmail, object : UserCallback {
                override fun onSuccess(user: UserModel) {
                    val userId = user.id

                    apiService.createService(userId, serviceRequest, object : ApiServiceImpl.ApiCallback {
                        override fun onSuccess(result: String) {
                            val intent = Intent(this@CreateServiceActivity, SportComplexActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onError(error: String) {
                            // Обробити помилку створення сервісу, якщо потрібно
                        }
                    })
                }

                override fun onError(error: String) {
                    // Обробити помилку отримання користувача, якщо потрібно
                }
            })
        }
    }

    private fun updateService() {
        val serviceName = serviceNameEditText.text.toString()

        // Переконайтеся, що serviceIdToUpdate має значення
        val serviceId = serviceIdToUpdate ?: return

        val updateRequest = ServiceUpdateRequest(id = serviceId, name = serviceName)

        apiService.updateService(updateRequest, object : ApiServiceImpl.ApiCallback {
            override fun onSuccess(result: String) {
                // Ваш код при успішному оновленні

                val intent = Intent(this@CreateServiceActivity, SportComplexActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onError(error: String) {
                // Обробіть помилку оновлення, якщо потрібно
            }
        })
    }
}