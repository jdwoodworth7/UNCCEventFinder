package com.example.test

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.ArrayList

class CalendarViewHolder(itemView: View, private val onItemListener: CalendarAdapter.OnItemListener, private val days: ArrayList<LocalDate?>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    val parentView: View = itemView.findViewById(R.id.parentView)


    init {
        itemView.setOnClickListener(this)
    }

    //Temporary way to click on day of month, will replace with event listeners once firebase exists
    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}