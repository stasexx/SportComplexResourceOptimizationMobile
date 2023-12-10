package com.example.sportcomplexresourceoptimizationmobile.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.activities.EquipmentActivity
import com.example.sportcomplexresourceoptimizationmobile.models.ServiceItem

class ServiceAdapter(private val serviceList: List<ServiceItem>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewServiceName)
        val viewEquipmentButton: Button = itemView.findViewById(R.id.buttonViewEquipments)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = serviceList[position]
        holder.nameTextView.text = service.name

        holder.viewEquipmentButton.setOnClickListener {
            onItemClick.invoke(service.id)
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}
