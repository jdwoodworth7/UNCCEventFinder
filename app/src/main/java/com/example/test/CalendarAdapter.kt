package com.example.test

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.ArrayList

class CalendarAdapter(private val days: ArrayList<LocalDate?>, private val onItemListener: OnItemListener) : RecyclerView.Adapter<CalendarViewHolder>() {
    //Creates the cells for the calendar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        //Each cell is 1/6 of the view
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view, onItemListener, days)
    }

    //Sets the color of the selected date to be light gray as long as its not null (off the calendar)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date: LocalDate? = days.get(position)
        if (date == null) {
            holder.dayOfMonth.text = ""
        } else {
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if (date == selectedDate) {
                holder.parentView.setBackgroundColor(Color.LTGRAY)
            }
        }
    }

    //Gets the day of the month counting upwards to create cells
    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: LocalDate?)
    }
}