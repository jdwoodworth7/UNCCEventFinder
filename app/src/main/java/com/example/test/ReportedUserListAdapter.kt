package com.example.test

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ReportedUserListAdapter(
    context: Context,
    private val dataSource: MutableList<UserData>
) : ArrayAdapter<UserData>(context, R.layout.activity_reported_users_list, dataSource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_reported_users_list, null)

        //get references to TextViews or other views in the inflated layout
        val lblUserName: TextView = view.findViewById(R.id.lblUserName)
        val lblUserEmail: TextView = view.findViewById(R.id.lblUserEmail)
        val lblUserReportCount: TextView = view.findViewById(R.id.lblUserReportCount)

        //set data to the views based on the position in the data source
        val user = getItem(position)
        val fullName = dataSource[position].firstname + "" + dataSource[position].lastname

        lblUserName.text = fullName
        lblUserEmail.text = dataSource[position].email
        lblUserReportCount.text = dataSource[position].reportCases.toString()

        view.setOnClickListener {
            startReportedUserActivity(position)
        }

        return view
    }

    fun startReportedUserActivity(position: Int) {
        val intent = Intent(context, ReportedUserActivity::class.java)
        val userData = dataSource[position]

        intent.putExtra("id", userData.id)
        intent.putExtra("firstname", userData.firstname)
        intent.putExtra("lastname", userData.lastname)
        intent.putExtra("email", userData.email)
        intent.putExtra("reportCases", userData.reportCases)

        context.startActivity(intent)
    }
}