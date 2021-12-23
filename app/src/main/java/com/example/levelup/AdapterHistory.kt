package com.example.levelup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterHistory (
    private val listHistory: ArrayList<CreateEventData>
) : RecyclerView.Adapter<AdapterHistory.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun review(position: Int)
        fun certificate(position: Int)
    }

    fun setItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _EventName: TextView = itemView.findViewById(R.id.HisttoryEventName)
        var _EventLocation: TextView = itemView.findViewById(R.id.HistoryEventLocation)
        var _EventDate: TextView = itemView.findViewById(R.id.HistoryEventDate)
        var _BtnReview: CardView = itemView.findViewById(R.id.ReviewBtn)
        var _BtnCertificate : CardView = itemView.findViewById(R.id.certificateBtn)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cardhistoryevent, parent, false)
        return ListViewHolder(view)
    }


    override fun onBindViewHolder(holder: AdapterHistory.ListViewHolder, position: Int) {
        var list = listHistory[position]
        holder._EventName.setText(list.title)
        holder._EventLocation.setText(list.location)
        holder._EventDate.setText(list.date)

        holder._BtnReview.setOnClickListener {
            onItemClickCallback.review(position)
        }

        holder._BtnCertificate.setOnClickListener {
            onItemClickCallback.certificate(position)
        }

    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}