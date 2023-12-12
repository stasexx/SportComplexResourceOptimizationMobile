package com.example.sportcomplexresourceoptimizationmobile.adapters

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.activities.CreateEquipmentActivity
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentItem

class EquipmentAdapter(
    private var equipmentList: List<EquipmentItem>,
    private val onItemClick: (String) -> Unit,
    private val onUpdateClick: (String, String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    // Оновлення списку обладнання
    fun updateData(newEquipmentList: List<EquipmentItem>) {
        equipmentList = newEquipmentList
        notifyDataSetChanged()
    }

    inner class EquipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val equipmentName: TextView = itemView.findViewById(R.id.textViewEquipmentName)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val createReservationButton: Button = itemView.findViewById(R.id.buttonCreateReservation)
        val updateButton: Button = itemView.findViewById(R.id.buttonUpdate)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipment, parent, false)
        return EquipmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = equipmentList[position]

        holder.equipmentName.text = equipment.name
        holder.statusTextView.text = if (!equipment.status) "Free" else "Busy"

        // Встановлення фону в залежності від статусу
        val statusDrawable =
            if (!equipment.status) R.drawable.bg_green_rounded else R.drawable.bg_red_rounded
        holder.statusTextView.setBackgroundResource(statusDrawable)

        // Зміна кольору тексту в залежності від статусу
        val textColor = if (!equipment.status) R.color.black else R.color.white
        holder.statusTextView.setTextColor(holder.itemView.resources.getColor(textColor))

        // Додайте інші дані до віджетів, якщо потрібно

        holder.createReservationButton.setOnClickListener {
            onItemClick.invoke(equipment.id)
        }

        // Обробка події для кнопки "Апдейт"
        holder.updateButton.setOnClickListener {
            onUpdateClick.invoke(equipment.id, equipment.name)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick.invoke(equipment.id)
        }
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }
}