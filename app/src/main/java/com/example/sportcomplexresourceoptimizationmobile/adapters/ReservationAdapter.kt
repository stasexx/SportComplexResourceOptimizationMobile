package com.example.sportcomplexresourceoptimizationmobile.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.ReservationItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserReservationAdapter(private var reservationList: List<ReservationItem>) :
    RecyclerView.Adapter<UserReservationAdapter.ReservationViewHolder>() {

    fun updateData(newReservationList: List<ReservationItem>) {
        // Сортування нового списку за спаданням дати кінця резервації
        val sortedList = newReservationList.sortedByDescending { it.endReservation }

        reservationList = sortedList
        notifyDataSetChanged()
    }

    private var is24HourFormat: Boolean = true

    // Додайте цей метод для зміни формату часу
    fun toggleTimeFormat() {
        is24HourFormat = !is24HourFormat
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

        // Конвертація часу з UTC в місцевий час
        val startTime = convertUtcToLocalTime(reservation.startReservation)
        val endTime = convertUtcToLocalTime(reservation.endReservation)

        holder.startTime.text = "Start: ${formatTime(startTime)}"
        holder.endTime.text = "End: ${formatTime(endTime)}"
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    private fun formatTime(time: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = if (is24HourFormat) {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US)
        }

        inputFormat.timeZone = TimeZone.getDefault()
        val date = inputFormat.parse(time)
        return outputFormat.format(date)
    }

    private fun convertUtcToLocalTime(utcTime: String): String {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")

        val localFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        localFormatter.timeZone = TimeZone.getDefault()

        return try {
            val utcDate = utcFormatter.parse(utcTime)
            localFormatter.format(utcDate)
        } catch (e: ParseException) {
            utcTime // Return original time
        }
    }
}