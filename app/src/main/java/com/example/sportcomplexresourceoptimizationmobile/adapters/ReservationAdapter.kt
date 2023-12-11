package com.example.sportcomplexresourceoptimizationmobile.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationItem

class UserReservationAdapter(private var reservationList: List<ReservationItem>) :
    RecyclerView.Adapter<UserReservationAdapter.ReservationViewHolder>() {

    fun updateData(newReservationList: List<ReservationItem>) {
        reservationList = newReservationList
        notifyDataSetChanged()
    }

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.textViewServiceName)
        val startTime: TextView = itemView.findViewById(R.id.textViewStartTime)
        val endTime: TextView = itemView.findViewById(R.id.textViewEndTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_reservation, parent, false)
        return ReservationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]

        holder.serviceName.text = reservation.equipmentName
        holder.startTime.text = "Start: ${reservation.startReservation}"
        holder.endTime.text = "End: ${reservation.endReservation}"
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }
}