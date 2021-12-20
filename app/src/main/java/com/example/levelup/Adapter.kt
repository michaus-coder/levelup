package com.example.levelup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//class Adapter {
//}


class adapterDashboard(private val listDashboardData: ArrayList<CreateEventData>):
    RecyclerView.Adapter<adapterDashboard.ListViewHolder>(){
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var _tv_dashboard_judul : TextView = itemView.findViewById(R.id.tv_dashboard_judul)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_dashboard, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var dataa = listDashboardData[position]
        holder._tv_dashboard_judul.setText(dataa.title)
    }

    override fun getItemCount(): Int {
        return listDashboardData.size
    }

}