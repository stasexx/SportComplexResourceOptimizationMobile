package com.example.sportcomplexresourceoptimizationmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem

class SportComplexAdminAdapter(
    private var sportComplexList: List<SportComplexItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SportComplexAdminAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewSportComplexName)
        val viewServicesButton: Button = itemView.findViewById(R.id.buttonViewServices)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sport_complex_admin, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sportComplex = sportComplexList[position]
        holder.nameTextView.text = sportComplex.name.toString()

        holder.viewServicesButton.setOnClickListener {
            onItemClick.invoke(sportComplex.id) // Передайте id при натисканні
        }
    }

    fun updateData(newList: List<SportComplexItem>) {
        sportComplexList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return sportComplexList.size
    }
}