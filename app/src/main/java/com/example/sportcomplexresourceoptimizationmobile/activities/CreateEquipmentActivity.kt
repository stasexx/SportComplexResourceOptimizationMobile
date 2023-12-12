package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentUpdateRequest
import com.example.sportcomplexresourceoptimizationmobile.services.EquipmentCallback
import com.google.android.material.snackbar.Snackbar

class CreateEquipmentActivity : AppCompatActivity() {

    private val apiService = ApiServiceImpl()
    private lateinit var editTextEquipmentName: EditText
    private lateinit var buttonCreateEquipment: Button
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_equipment)

        // Отримання ідентифікатора та імені обладнання з інтенту
        val equipmentId = intent.getStringExtra("EQUIPMENT_ID")
        val equipmentName = intent.getStringExtra("EQUIPMENT_NAME")

        editTextEquipmentName = findViewById(R.id.editTextEquipmentName)
        buttonCreateEquipment = findViewById(R.id.buttonCreateEquipment)

        // Якщо ідентифікатор обладнання не пустий, то це оновлення, а не створення нового
        isUpdate = !equipmentId.isNullOrEmpty()

        // Встановлення імені обладнання в текстовий рядок
        editTextEquipmentName.setText(equipmentName)

        val serviceId = intent.getStringExtra("SERVICE_ID")
        val userId = intent.getStringExtra("USER_ID")
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)

        Log.d("Я ТУТ", "Creating equipment...")
        buttonCreateEquipment.setOnClickListener {
            val equipmentName = editTextEquipmentName.text.toString().trim()
            if (equipmentName.isNotEmpty()) {
                // Виклик методу створення обладнання чи оновлення
                if (isUpdate) {
                    updateEquipment(equipmentId, equipmentName)
                } else {
                    createEquipment(serviceId, userId, equipmentName)
                }
            } else {
                Snackbar.make(
                    buttonCreateEquipment,
                    "Equipment name cannot be empty",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateEquipment(equipmentId: String?, equipmentName: String) {
        val updateRequest = EquipmentUpdateRequest(id = equipmentId.orEmpty(), name = equipmentName)
        Log.d("updateEquipment", "updateRequest : $updateRequest")
        apiService.updateEquipment(updateRequest, object : ApiServiceImpl.ApiCallback {
            override fun onSuccess(result: String) {
                // Обробте успішне оновлення обладнання
                Snackbar.make(buttonCreateEquipment, "Equipment updated successfully.", Snackbar.LENGTH_SHORT).show()
                val intent = Intent(this@CreateEquipmentActivity, SportComplexActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onError(error: String) {
                // Обробте помилку оновлення обладнання, якщо потрібно
                Snackbar.make(buttonCreateEquipment, "Error: $error", Snackbar.LENGTH_SHORT).show()
            }
        })
    }


    private fun createEquipment(serviceId: String?, userId: String?, equipmentName: String) {

        Log.d("Creating equipment", "Service id : $serviceId, userId: $userId")
        apiService.createEquipment(serviceId, userId, equipmentName, object :
            ApiServiceImpl.ApiCallback {
            override fun onSuccess(result: String) {
                // Вибудувати інтент для повторного завантаження EquipmentActivity
                val intent = Intent(this@CreateEquipmentActivity, SportComplexActivity::class.java)
                // Передати SERVICE_ID, якщо він був переданий в оригінальному інтенті
                intent.putExtra("SERVICE_ID", intent.getStringExtra(serviceId))
                Log.d("Creating equipment", "Service id : $serviceId")
                startActivity(intent)
                finish()
            }

            override fun onError(error: String) {
                // Обробіть помилку створення обладнання, якщо потрібно
                Snackbar.make(buttonCreateEquipment, "Error: $error", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
