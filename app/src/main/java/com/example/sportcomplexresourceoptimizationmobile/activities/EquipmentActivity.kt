package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.adapters.EquipmentAdapter
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentItem
import com.example.sportcomplexresourceoptimizationmobile.services.EquipmentCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EquipmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        recyclerView = findViewById(R.id.recyclerViewEquipment)
        recyclerView.layoutManager = LinearLayoutManager(this)

        equipmentAdapter = EquipmentAdapter(emptyList(), ::openReservationActivity)

        recyclerView.adapter = equipmentAdapter

        fetchEquipments()
    }

    private fun fetchEquipments() {
        val serviceId = intent.getStringExtra("SERVICE_ID") ?: ""
        apiService.getEquipmentsForSportComplex(
            serviceId = serviceId,
            pageNumber = 1,
            pageSize = 20,
            callback = object : EquipmentCallback {
                override fun onSuccess(result: List<EquipmentItem>) {
                    // Оновіть дані в адаптері з відомістю про статус
                    updateEquipmentStatus(result)
                }

                override fun onError(errorMessage: String) {
                    // Обробка помилки, якщо потрібно
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


    private fun openReservationActivity(equipmentId: String) {
        val intent = Intent(this, ReservationActivity::class.java)
        intent.putExtra("EQUIPMENT_ID", equipmentId)
        startActivity(intent)
    }
}
