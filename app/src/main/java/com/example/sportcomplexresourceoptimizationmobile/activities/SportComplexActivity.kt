package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.sportcomplexresourceoptimizationmobile.services.SportComplexService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SportComplexActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sportComplexAdapter: SportComplexAdapter
    private val apiService = ApiServiceImpl()
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_complex)

        recyclerView = findViewById(R.id.recyclerViewSportComplex)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Додайте слухач натискання на елемент до адаптера
        sportComplexAdapter = SportComplexAdapter(emptyList()) { sportComplexId ->
            // Обробляйте натискання на елемент тут, наприклад, викликаєте нову активність
            openServiceActivity(sportComplexId)
        }
        recyclerView.adapter = sportComplexAdapter

        fetchSportComplexes()
    }

    private fun fetchSportComplexes() {
        println("ФЕЕЕЕЕТЧ")
        apiService.getSportComplexes(1, 20, object : SportComplexService {
            override fun onSuccess(result: List<SportComplexItem>) {
                println("ПРИЙШЛО")
                println(result)
                val sportComplexList = parseSportComplexList(result)

                // Оновіть адаптер і встановіть його в RecyclerView
                sportComplexAdapter = SportComplexAdapter(sportComplexList) { sportComplexId ->
                    openServiceActivity(sportComplexId)
                }
                recyclerView.adapter = sportComplexAdapter
            }

            override fun onError(error: String) {
                // Обробте помилку, якщо потрібно
            }
        })
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
}