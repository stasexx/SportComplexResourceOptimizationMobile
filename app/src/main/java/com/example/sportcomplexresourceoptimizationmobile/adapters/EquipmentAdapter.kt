package com.example.sportcomplexresourceoptimizationmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentItem

class EquipmentAdapter(
    private val equipmentList: List<EquipmentItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewEquipmentName)
        val buttonCreateReservation: Button = itemView.findViewById(R.id.buttonCreateReservation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_equipment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.nameTextView.text = equipment.name

        holder.buttonCreateReservation.setOnClickListener {
            onItemClick.invoke(equipment.id) // Передаємо equipment_id при натисканні
        }
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }
}
