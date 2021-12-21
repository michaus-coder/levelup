package com.example.levelup

import android.content.Intent
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

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data : CreateEventData)
//        fun delData(position: Int)
        fun delDataDatabase(position: Int, kirimData : CreateEventData)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var removedPosition : Int ? = null

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var _tv_dashboard_judul : TextView = itemView.findViewById(R.id.tv_dashboard_judul)
        var _btn_edit_dashboard : Button = itemView.findViewById(R.id.btn_edit_dashboard)
        var _btn_delete_dashboard : Button = itemView.findViewById(R.id.btn_delete_dashboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_dashboard, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var dataa = listDashboardData[position]
        holder._tv_dashboard_judul.setText(dataa.title)

        holder._btn_delete_dashboard.setOnClickListener {
            listDashboardData.removeAt(position)
            removedPosition = position
            notifyDataSetChanged()
        }

        //Firebase Delete
        holder._btn_delete_dashboard.setOnClickListener {
            onItemClickCallback.delDataDatabase(position, listDashboardData[position])
        }

    }

    override fun getItemCount(): Int {
        return listDashboardData.size
    }

    fun getRemoveItemPosition() : Int? {
        var position = removedPosition
        return position;
    }

}