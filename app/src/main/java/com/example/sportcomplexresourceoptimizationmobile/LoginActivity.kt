package com.example.sportcomplexresourceoptimizationmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.example.sportcomplexresourceoptimizationmobile.activities.SportComplexActivity
import com.example.sportcomplexresourceoptimizationmobile.models.LoginModel
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var sharedPreferences: SharedPreferences // Додайте цей рядок

    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        registerButton = findViewById(R.id.buttonRegister)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) // Додайте цей рядок

        // Перевірте, чи вже є збережений email
        val savedEmail = sharedPreferences.getString("email", "")
        if (savedEmail?.isNotEmpty() == true) {
            // Якщо є, встановіть його в поле для вводу
            usernameEditText.setText(savedEmail)
        }

        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Збережіть email у SharedPreferences при успішному логіні
            apiService.loginUser(email, password, object : ApiServiceImpl.ApiCallback {
                override fun onSuccess(result: String) {
                    println("УСПІШНО")
                    println(result)

                    // Збереження email у SharedPreferences
                    sharedPreferences.edit().putString("email", email).apply()

                    val intent = Intent(this@LoginActivity, SportComplexActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onError(error: String) {
                    println("ПОМИЛКААААА")
                    println(error)
                    Snackbar.make(it, "Login failed. Please try again.", Snackbar.LENGTH_LONG).show()
                }
            })
        }

        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistryActivity::class.java)
            startActivity(intent)
        }
    }
}