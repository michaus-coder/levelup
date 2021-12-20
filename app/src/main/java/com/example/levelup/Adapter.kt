//package com.example.levelup
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.Spinner
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
////class Adapter {
////}
//
//
//class adapterDashboard(private val listDashboardData: ArrayList<CreateEventData>):
//    RecyclerView.Adapter<adapterDashboard.ListViewHolder>(){
//    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        var _tv_dashboard_atas : EditText = itemView.findViewById(R.id.tv_dashboard_atas)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rc_events, parent, false)
//        return ListViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        var dataa = listDashboardData[position]
//        holder._tv_layout_kategori.setText(dataa.Nominal)
//        holder._tv_layout_nilai.setText(dataa.Kategori)
//        holder._tv_timeStamp.setText(dataa.Waktu)
//    }
//
//    override fun getItemCount(): Int {
//        return listCEData.size
//    }
//
//}