package com.example.sportcomplexresourceoptimizationmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexItem
import com.example.sportcomplexresourceoptimizationmobile.models.SportComplexModel

class SportComplexAdapter(private val sportComplexList: List<SportComplexItem>,
                          private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<SportComplexAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewSportComplexName)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewSportComplexEmail)
        val viewServicesButton: Button = itemView.findViewById(R.id.buttonViewServices)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sport_complex, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sportComplex = sportComplexList[position]
        holder.nameTextView.text = sportComplex.name.toString()
        holder.emailTextView.text = sportComplex.email

        holder.viewServicesButton.setOnClickListener {
            onItemClick.invoke(sportComplex.id) // Передайте id при натисканні
        }
    }

    override fun getItemCount(): Int {
        return sportComplexList.size
    }
}