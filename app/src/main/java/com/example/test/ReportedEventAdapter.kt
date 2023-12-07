package com.example.test

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import org.w3c.dom.Text

class ReportedEventAdapter(
    context: Context,
    private val dataSource: MutableList<EventReport>,
) : ArrayAdapter<EventReport>(context, R.layout.activity_reports_event, dataSource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_reports_event, null)

        //get references to TextViews or other views in the inflated layout
        val lblCategory : TextView = view.findViewById(R.id.lblCategory)
        val lblReportDate : TextView = view.findViewById(R.id.lblReportDate)

        //set data to the views based on the position in the data source
        lblCategory.text = dataSource[position].category
        lblReportDate.text = dataSource[position].reportedDate

        view.setOnClickListener{
            startReportDetailsActivity(position)
        }

        return view
    }

    fun startReportDetailsActivity(position: Int) {
        val intent = Intent(context, ReportDetailsActivity::class.java)
        val reportData = dataSource[position]

        intent.putExtra("id", reportData.id)
        intent.putExtra("eventAuthorId", reportData.eventAuthorId) //author of the reported event
        intent.putExtra("reportAuthorId", reportData.reportAuthorId)
        intent.putExtra("reportedDate", reportData.reportedDate)
        intent.putExtra("category", reportData.category)
        intent.putExtra("details", reportData.details)
        intent.putExtra("eventId", reportData.eventId)
        intent.putExtra("eventName" , reportData.eventName)

        context.startActivity(intent)
    }
}