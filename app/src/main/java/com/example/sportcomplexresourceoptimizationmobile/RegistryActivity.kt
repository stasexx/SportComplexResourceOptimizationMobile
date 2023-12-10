package com.example.sportcomplexresourceoptimizationmobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sportcomplexresourceoptimizationmobile.models.RegisterModel
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem

class RegistryActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registry)

        firstNameEditText = findViewById(R.id.editTextFirstName)
        lastNameEditText = findViewById(R.id.editTextLastName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextRegisterPassword)
        registerButton = findViewById(R.id.buttonRegister)

        // Переставлено сюди
        registerButton.setOnClickListener {
            val registerModel = RegisterModel(
                firstNameEditText.text.toString(),
                lastNameEditText.text.toString(),
                phoneEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )

            apiService.registerUser(registerModel, object : ApiServiceImpl.ApiCallback {
                override fun onSuccess(result: String) {
                    println("Registration successful")
                }

                override fun onError(error: String) {
                    println("Registration failed: $error")
                }
            })
        }
    }
}