package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.adapters.ServiceAdapter
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceItem
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.ServiceCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback

class ServiceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private val apiService = ApiServiceImpl()
    var isAdmin = false
    val deleteService: (String) -> Unit = { serviceId ->
        apiService.deleteService(serviceId, object : ApiServiceImpl.ApiCallback {
            override fun onSuccess(result: String) {
                // Обробити успішне видалення, якщо потрібно
                fetchServices()
            }

            override fun onError(error: String) {
                // Обробити помилку видалення, якщо потрібно
            }
        })
    }
    val updateService: (String, String) -> Unit = { serviceId, serviceName ->
        openServiceUpdateActivity(serviceId, serviceName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        val createServiceButton: Button = findViewById(R.id.createServiceButton)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)
        if (userEmail != null) {
            // Отримати дані про користувача з сервера
            apiService.getUserByEmail(userEmail, object : UserCallback {
                override fun onSuccess(user: UserModel) {
                    // Перевірити, чи у користувача є роль "Admin"
                    isAdmin = user.roles.any { it.name == "Admin" || it.name == "Owner"}
                    invalidateOptionsMenu()
                    if (isAdmin) {
                        createServiceButton.visibility = View.VISIBLE
                    } else {
                        createServiceButton.visibility = View.GONE
                    }
                    if (isAdmin) {
                        // Відображення кнопки "Create Sportcomplex"

                        invalidateOptionsMenu()

                        val userId = user.id
                        val userPhone = user.phone
                        serviceAdapter = ServiceAdapter(
                            emptyList(),
                            { serviceId -> openEquipmentActivity(serviceId) },
                            { serviceId -> deleteService(serviceId) },
                            updateService,
                            isAdmin
                        )


                        recyclerView.adapter = serviceAdapter
                    }
                }

                override fun onError(error: String) {
                    // Обробте помилку, якщо потрібно
                }
            })
        }

        val sportComplexId = intent.getStringExtra("SPORT_COMPLEX_ID") ?: ""

        createServiceButton.setOnClickListener {
            println("ЗАЙШОВ")
            openServiceCreateActivity(sportComplexId)
        }

        recyclerView = findViewById(R.id.recyclerViewService)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Оновлено конструктор, щоб передати onItemClick
        serviceAdapter = ServiceAdapter(
            emptyList(),
            { serviceId -> openEquipmentActivity(serviceId) },
            { serviceId -> deleteService(serviceId) },
            updateService,
            isAdmin
        )


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
                    println(result)
                    serviceAdapter.updateData(result)
                    serviceAdapter.notifyDataSetChanged()
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
    private fun openServiceCreateActivity(sportComplexId: String) {
        val intent = Intent(this, CreateServiceActivity::class.java)
        intent.putExtra("SPORT_COMPLEX_ID", sportComplexId)
        startActivity(intent)
    }

    private fun openServiceUpdateActivity(serviceId: String, serviceName: String) {
        val intent = Intent(this, CreateServiceActivity::class.java)
        intent.putExtra("SERVICE_ID", serviceId)
        intent.putExtra("SERVICE_NAME", serviceName)
        startActivity(intent)
    }

    fun openDrawer(view: View) {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(GravityCompat.START)
    }
}

