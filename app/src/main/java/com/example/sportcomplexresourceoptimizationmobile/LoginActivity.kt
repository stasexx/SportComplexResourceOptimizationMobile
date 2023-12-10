package com.example.sportcomplexresourceoptimizationmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.activities.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Викликати метод для відправки POST-запиту з логіном та паролем
            apiService.loginUser(username, password, object : ApiServiceImpl.ApiCallback {

                override fun onSuccess(result: String) {
                    // Обробка успішної відповіді після логіну
                    println("УСПІШНО")
                    println(result)
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
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
    }
}
