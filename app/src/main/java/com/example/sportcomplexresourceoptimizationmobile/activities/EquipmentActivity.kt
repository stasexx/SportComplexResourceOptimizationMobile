package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.adapters.EquipmentAdapter
import com.example.sportcomplexresourceoptimizationmobile.adapters.ServiceAdapter
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentItem
import com.example.sportcomplexresourceoptimizationmobile.models.UserModel
import com.example.sportcomplexresourceoptimizationmobile.services.EquipmentCallback
import com.example.sportcomplexresourceoptimizationmobile.services.UserCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EquipmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter
    private val apiService = ApiServiceImpl()
    var userId = "";
    var isAdmin = false
    var serviceId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        recyclerView = findViewById(R.id.recyclerViewEquipment)
        recyclerView.layoutManager = LinearLayoutManager(this)
        serviceId = intent.getStringExtra("SERVICE_ID") ?: ""
        val createEquipmentButton: Button = findViewById(R.id.createEquipmentButton)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)
        if (userEmail != null) {
            // Отримати дані про користувача з сервера
            apiService.getUserByEmail(userEmail, object : UserCallback {
                override fun onSuccess(user: UserModel) {
                    // Перевірити, чи у користувача є роль "Admin"
                    isAdmin = user.roles.any { it.name == "Admin" || it.name == "Owner"}
                    invalidateOptionsMenu()
                    userId = user.id
                    if (isAdmin) {
                        createEquipmentButton.visibility = View.VISIBLE
                    } else {
                        createEquipmentButton.visibility = View.GONE
                    }
                    if (isAdmin) {
                        // Відображення кнопки "Create Sportcomplex"
                        createEquipmentButton.visibility = View.VISIBLE

                        // Отримання ідентифікатора та імені обладнання з інтенту
                        val equipmentId = intent.getStringExtra("EQUIPMENT_ID")
                        val equipmentName = intent.getStringExtra("EQUIPMENT_NAME")

                        equipmentAdapter = EquipmentAdapter(
                            emptyList(),
                            ::openReservationActivity,
                            { equipmentId, equipmentName -> updateEquipment(equipmentId, equipmentName) },
                            ::deleteEquipment
                        )

                        recyclerView.adapter = equipmentAdapter

                        fetchEquipments(serviceId)
                    }
                }

                override fun onError(error: String) {
                    // Обробте помилку, якщо потрібно
                }
            })
        }

        createEquipmentButton.setOnClickListener {
            val intent = Intent(this, CreateEquipmentActivity::class.java)
            // Передати сервіс айді та юзер айді
            intent.putExtra("SERVICE_ID", serviceId)
            intent.putExtra("USER_ID", userId)

            startActivity(intent)
        }

        equipmentAdapter = EquipmentAdapter(
            emptyList(),
            ::openReservationActivity,
            { equipmentId, equipmentName -> updateEquipment(equipmentId, equipmentName) },
            ::deleteEquipment
        )

        recyclerView.adapter = equipmentAdapter

        fetchEquipments(serviceId)

        fetchEquipments(serviceId)
    }

    private fun updateEquipment(equipmentId: String, equipmentName: String) {
        val intent = Intent(this, CreateEquipmentActivity::class.java).apply {
            putExtra("EQUIPMENT_ID", equipmentId)
            putExtra("EQUIPMENT_NAME", equipmentName)
        }
        startActivity(intent)
    }


    private fun fetchEquipments(serviceId: String ) {
        apiService.getEquipmentsForSportComplex(
            serviceId = serviceId,
            pageNumber = 1,
            pageSize = 20,
            callback = object : EquipmentCallback {
                override fun onSuccess(result: List<EquipmentItem>) {
                    Log.d("Fetching equipments", "Service id : $serviceId")
                    updateEquipmentStatus(result)
                }

                override fun onError(errorMessage: String) {
                    Log.d("Fetching equipments", "Service id : $serviceId Error : $errorMessage")
                }
            }
        )
    }

    private fun updateEquipmentStatus(equipmentList: List<EquipmentItem>) {
        val updatedEquipmentList = mutableListOf<EquipmentItem>()

        equipmentList.forEach { equipment ->
            apiService.getEquipmentStatus(equipment.id, object : ApiServiceImpl.ApiCallback {
                override fun onSuccess(result: String) {
                    println("РЕЗУЛЬТ" + result)
                    equipment.status = result.toBoolean()
                    updatedEquipmentList.add(equipment)

                    println("РЕЗУЛЬТ" + result + "ЗАЙЗ" + updatedEquipmentList.size + " = " + equipmentList.size)
                    if (updatedEquipmentList.size == equipmentList.size) {
                        // Використовуйте корутину для виклику оновлення адаптера на головному потоці
                        GlobalScope.launch(Dispatchers.Main) {
                            equipmentAdapter.updateData(updatedEquipmentList)
                        }
                    }
                }

                override fun onError(error: String) {
                    // Обробка помилки, якщо потрібно
                }
            })
        }
    }

    private fun deleteEquipment(equipmentId: String) {
        apiService.deleteEquipment(equipmentId, object : ApiServiceImpl.ApiCallback {
            override fun onSuccess(result: String) {
                // Handle success, if needed
                fetchEquipments(serviceId) // Refresh the equipment list after deletion
            }

            override fun onError(error: String) {
                // Handle error, if needed
            }
        })
    }


    private fun openReservationActivity(equipmentId: String) {
        val intent = Intent(this, ReservationActivity::class.java)
        intent.putExtra("EQUIPMENT_ID", equipmentId)
        startActivity(intent)
    }

    fun openDrawer(view: View) {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(GravityCompat.START)
    }
}
