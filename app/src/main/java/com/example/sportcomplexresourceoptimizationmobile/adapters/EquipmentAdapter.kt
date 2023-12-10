package com.example.sportcomplexresourceoptimizationmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.EquipmentItem

class EquipmentAdapter(private val equipmentList: List<EquipmentItem>) :
    RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewEquipmentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_equipment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.nameTextView.text = equipment.name
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }
}
