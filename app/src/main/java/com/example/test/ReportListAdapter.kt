package com.example.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import org.w3c.dom.Text

class ReportListAdapter(
    context: Context,
    private val dataSource: MutableList<EventData>,
    private val authorName: String
) : ArrayAdapter<EventData>(context, R.layout.activity_reports_by_user, dataSource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_reports_by_user, null)

        //Used for event image
        val imageUrl: String? = dataSource[position].imageUri

        //get references to TextViews or other views in the inflated layout
        val icon : ImageView = view.findViewById(R.id.imgEvent)
        val lblEventName : TextView = view.findViewById(R.id.lblEventName)
        val lblAuthorName : TextView = view.findViewById(R.id.lblAuthorName)
        val lblUserReportCount : TextView = view.findViewById(R.id.lblEventReportCount)

        val reportCount = dataSource[position].reportCount

        //set data to the views based on the position in the data source
        icon.load(imageUrl)
        lblEventName.text = dataSource[position].title
        lblAuthorName.text = "by $authorName"
        lblUserReportCount.text = reportCount.toString()

        //adjusts the color of the report count by number
        when (reportCount) {
            in 1..4 -> {
                lblUserReportCount.setTextColor(ContextCompat.getColor(context, R.color.color_light_warning))
            }
            in 5..9 -> {
                lblUserReportCount.setTextColor(ContextCompat.getColor(context, R.color.color_warning))
            }
            else -> {
                lblUserReportCount.setTextColor(ContextCompat.getColor(context, R.color.color_danger))
            }
        }

        return view
    }
}