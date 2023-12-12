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

class ServiceAdapter(
    private var serviceList: List<ServiceItem>,
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onUpdateClick: (String, String) -> Unit,
    private val isAdminOrOwner: Boolean
) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceNameTextView: TextView = itemView.findViewById(R.id.textViewServiceName)
        val viewEquipmentsButton: Button = itemView.findViewById(R.id.buttonViewEquipments)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
        val updateButton: Button = itemView.findViewById(R.id.buttonUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceNameTextView.text = service.name.toString()

        holder.viewEquipmentsButton.setOnClickListener {
            onItemClick.invoke(service.id)
        }
        println("isAdminOrOwner: $isAdminOrOwner for service with id: ${service.id}")
        if (isAdminOrOwner) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.updateButton.visibility = View.VISIBLE

            holder.deleteButton.setOnClickListener {
                onDeleteClick.invoke(service.id)
            }

            holder.updateButton.setOnClickListener {
                onUpdateClick.invoke(service.id, service.name)
            }
        } else {
            holder.deleteButton.visibility = View.GONE
            holder.updateButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    fun updateData(newData: List<ServiceItem>) {
        serviceList = newData
        notifyDataSetChanged()
    }
}
