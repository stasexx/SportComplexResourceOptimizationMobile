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

class EquipmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        recyclerView = findViewById(R.id.recyclerViewEquipment)
        recyclerView.layoutManager = LinearLayoutManager(this)

        equipmentAdapter = EquipmentAdapter(emptyList()) { equipmentId ->
            openReservationActivity(equipmentId)
        }

        recyclerView.adapter = equipmentAdapter

        fetchEquipments()
    }

    private fun fetchEquipments() {
        println("ПРИЙШЛО")
        val serviceId = intent.getStringExtra("SERVICE_ID") ?: ""
        apiService.getEquipmentsForSportComplex(
            serviceId = serviceId,
            pageNumber = 1,
            pageSize = 20,
            callback = object : EquipmentCallback {
                override fun onSuccess(result: List<EquipmentItem>) {
                    equipmentAdapter = EquipmentAdapter(result) { equipmentId ->
                        openReservationActivity(equipmentId)
                    }
                    recyclerView.adapter = equipmentAdapter
                }

                override fun onError(errorMessage: String) {
                    // Обробка помилки, якщо потрібно
                }
            }
        )
    }

    private fun openReservationActivity(equipmentId: String) {
        val intent = Intent(this, ReservationActivity::class.java)
        intent.putExtra("EQUIPMENT_ID", equipmentId)
        startActivity(intent)
    }
}
