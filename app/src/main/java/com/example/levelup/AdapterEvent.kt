package com.example.levelup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterEvent (
    private val listEvent: ArrayList<EventDetail>
) : RecyclerView.Adapter<AdapterEvent.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun detailEvent()
        fun joinEvent()

    }

    fun setItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _EventTitle: TextView = itemView.findViewById(R.id.titleEvent)
        var _EventDate: TextView = itemView.findViewById(R.id.dateEvent)
        var _EventLocation: TextView = itemView.findViewById(R.id.locationEvent)
        var _EventTime:TextView = itemView.findViewById(R.id.timeEvent)
        var _EventPrice:TextView = itemView.findViewById(R.id.priceEvent)
        var _BtnDetailEvent: Button = itemView.findViewById(R.id.btnDetailEvent)
        var _BtnJoinEvent: Button = itemView.findViewById(R.id.btnJoinEvent)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardeventrv, parent, false)
        return ListViewHolder(view)
    }


    override fun onBindViewHolder(holder: AdapterEvent.ListViewHolder, position: Int) {
        var note = listEvent[position]
        holder._EventTitle.setText(note.eventName)
        holder._EventLocation.setText(note.eventLocation)
        holder._EventDate.setText(note.eventDate)
        holder._EventTime.setText(note.eventTime)
        holder._EventPrice.setText(note.eventPrice)


        holder._BtnDetailEvent.setOnClickListener {
            onItemClickCallback.detailEvent()

        }
        holder._BtnJoinEvent.setOnClickListener {
            onItemClickCallback.joinEvent()

        }

    }

    override fun getItemCount(): Int {
        return listEvent.size
    }
}