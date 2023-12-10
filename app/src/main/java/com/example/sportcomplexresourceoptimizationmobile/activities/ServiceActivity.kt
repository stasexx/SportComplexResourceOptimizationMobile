package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.adapters.ServiceAdapter
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceItem
import com.example.sportcomplexresourceoptimizationmobile.services.ServiceCallback

class ServiceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        recyclerView = findViewById(R.id.recyclerViewService)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Оновлено конструктор, щоб передати onItemClick
        serviceAdapter = ServiceAdapter(emptyList()) { serviceId ->
            openEquipmentActivity(serviceId)
        }

        recyclerView.adapter = serviceAdapter

        fetchServices()
    }

    private fun fetchServices() {
        println("ПРИЙШЛО")
        val sportComplexId = intent.getStringExtra("SPORT_COMPLEX_ID") ?: ""
        apiService.getServicesForSportComplex(
            sportComplexId = sportComplexId,
            pageNumber = 1,
            pageSize = 10,
            callback = object : ServiceCallback {
                override fun onSuccess(result: List<ServiceItem>) {
                    // Оновлення адаптера і встановлення його в RecyclerView
                    serviceAdapter = ServiceAdapter(result) { serviceId ->
                        openEquipmentActivity(serviceId)
                    }
                    recyclerView.adapter = serviceAdapter
                }

                override fun onError(errorMessage: String) {
                    // Обробка помилки, якщо потрібно
                }
            }
        )
    }

    private fun openEquipmentActivity(serviceId: String) {
        val intent = Intent(this, EquipmentActivity::class.java)
        intent.putExtra("SERVICE_ID", serviceId)
        startActivity(intent)
    }
}

