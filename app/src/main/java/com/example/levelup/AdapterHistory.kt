package com.example.levelup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterHistory (
    private val listHistory: ArrayList<HistoryEventListItem>
) : RecyclerView.Adapter<AdapterHistory.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun review(position: Int)
    }

    fun setItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _EventName: TextView = itemView.findViewById(R.id.HisttoryEventName)
        var _EventLocation: TextView = itemView.findViewById(R.id.HistoryEventLocation)
        var _EventDate: TextView = itemView.findViewById(R.id.HistoryEventDate)
        var _BtnReview: Button = itemView.findViewById(R.id.ReviewBtn)
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
        var note = listHistory[position]
//        holder._EventName.setText(note.HistoryeventName)
//        holder._EventLocation.setText(note.HistoryeventLocation)
//        holder._EventDate.setText(note.HistoryeventDate)

        holder._BtnReview.setOnClickListener {
            onItemClickCallback.review(position)

        }

    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}