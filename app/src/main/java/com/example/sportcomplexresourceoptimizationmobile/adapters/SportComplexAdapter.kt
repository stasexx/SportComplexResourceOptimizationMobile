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

class SportComplexAdapter(
    private var sportComplexList: List<SportComplexItem>,
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onUpdateClick: (String) -> Unit,
    private var isAdmin: Boolean
) :
    RecyclerView.Adapter<SportComplexAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewSportComplexName)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewSportComplexEmail)
        val viewServicesButton: Button = itemView.findViewById(R.id.buttonViewServices)
        val cityTextView: TextView = itemView.findViewById(R.id.textViewSportComplexCity)
        val addressTextView: TextView = itemView.findViewById(R.id.textViewSportComplexAddress)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewSportComplexDescription)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
        val buttonUpdate: Button = itemView.findViewById(R.id.buttonUpdate)
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
        holder.cityTextView.text = sportComplex.city
        holder.addressTextView.text = sportComplex.address
        holder.descriptionTextView.text = sportComplex.description
        println("ПЕРЕВІРКА АДМІНКИ" + isAdmin)
        if (isAdmin) {
            holder.buttonDelete.setOnClickListener {
                onDeleteClick.invoke(sportComplex.id)
            }

            holder.buttonUpdate.setOnClickListener {
                onUpdateClick.invoke(sportComplex.id)
            }

        } else {
            holder.buttonDelete.visibility = View.GONE
            holder.buttonUpdate.visibility = View.GONE
        }

        holder.viewServicesButton.setOnClickListener {
            onItemClick.invoke(sportComplex.id) // Передайте id при натисканні
        }
        holder.viewServicesButton.setOnClickListener {
            onItemClick.invoke(sportComplex.id)
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