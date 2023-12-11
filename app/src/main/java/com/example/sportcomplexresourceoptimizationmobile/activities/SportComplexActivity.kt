package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.adapters.SportComplexAdapter
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.SportComplexService
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SportComplexActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sportComplexAdapter: SportComplexAdapter
    private val apiService = ApiServiceImpl()
    private val gson = Gson()
    private var isAdmin = false
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_complex)
        recyclerView = findViewById(R.id.recyclerViewSportComplex)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val navigationView: NavigationView = findViewById(R.id.navigationView)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)
        if (userEmail != null) {
            // Отримати дані про користувача з сервера
            apiService.getUserByEmail(userEmail, object : UserCallback {
                override fun onSuccess(user: UserModel) {
                    // Перевірити, чи у користувача є роль "Admin"
                    isAdmin = user.roles.any { it.name == "Admin" }
                    invalidateOptionsMenu()
                    if (isAdmin) {
                        // Відображення кнопки "Create Sportcomplex"
                        val menu = navigationView.menu

                        invalidateOptionsMenu()

                        val userId = user.id
                        val userPhone = user.phone
                        // інші дані користувача
                    }
                }

                override fun onError(error: String) {
                    // Обробте помилку, якщо потрібно
                }
            })
        }

        val createSportComplexButton: Button = findViewById(R.id.createSportComplexButton)

        createSportComplexButton.setOnClickListener {
            val intent = Intent(this, CreateSportComplexActivity::class.java)
            startActivity(intent)
        }

        sportComplexAdapter = SportComplexAdapter(
            emptyList(),
            { sportComplexId -> openServiceActivity(sportComplexId) },
            { sportComplexId -> /* delete logic here */ },
            { sportComplexId -> /* update logic here */ },
            false
        )
        recyclerView.adapter = sportComplexAdapter


        fetchSportComplexes()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    val intent = Intent(this, UsersReservationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item2 -> {
                    true
                }
                R.id.createReservationButton -> {
                    println("СТВОРЕННЯ")
                    val intent = Intent(this, CreateSportComplexActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


    private fun fetchSportComplexes() {
        println("ФЕЕЕЕЕТЧ")
        apiService.getSportComplexes(1, 20, object : SportComplexService {
            override fun onSuccess(result: List<SportComplexItem>) {
                println("ПРИЙШЛО")
                println(result)
                val sportComplexList = parseSportComplexList(result)

                // Оновіть адаптер і встановіть його в RecyclerView
                sportComplexAdapter = SportComplexAdapter(sportComplexList,
                    { sportComplexId -> openServiceActivity(sportComplexId) },
                    { sportComplexId -> deleteSportComplex(sportComplexId) },
                    { sportComplexId -> updateSportComplex(sportComplexId) },
                    isAdmin
                )
                recyclerView.adapter = sportComplexAdapter

                // Поновлення меню
                invalidateOptionsMenu()
            }

            override fun onError(error: String) {
                // Обробте помилку, якщо потрібно
            }
        })
    }

    private fun deleteSportComplex(sportComplexId: String) {
        // Логіка видалення спорткомплексу
    }

    private fun updateSportComplex(sportComplexId: String) {
        // Логіка оновлення спорткомплексу
    }

    private fun openServiceActivity(sportComplexId: String) {
        // Викликайте нову активність і передавайте sportComplexId як параметр
        val intent = Intent(this, ServiceActivity::class.java)
        intent.putExtra("SPORT_COMPLEX_ID", sportComplexId)
        startActivity(intent)
    }

    fun openDrawer(view: View) {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(GravityCompat.START)
    }
    private fun parseSportComplexList(response: List<SportComplexItem>): List<SportComplexItem> {
        return response
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.navigation_menu, menu)

        println("ОПРАЦЮВАННЯ")


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("я тута")
        return when (item.itemId) {
            R.id.menu_item1 -> {
                println("РЕЗЕРВАЦІЇ ЙОПТА")
                val intent = Intent(this, UsersReservationsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item2 -> {
                // Обробка натискання на "Log Out"
                // Додайте код для виходу з облікового запису або іншої логіки виходу
                true
            }
            R.id.createReservationButton -> {
                println("СТВОРЕННЯ")
                val intent = Intent(this, CreateSportComplexActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}