package com.example.test

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarViewHolder(itemView: View, private val onItemListener: CalendarAdapter.OnItemListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)

    init {
        itemView.setOnClickListener(this)
    }

    //Temporary way to click on day of month, will replace with event listeners once firebase exists
    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }
}