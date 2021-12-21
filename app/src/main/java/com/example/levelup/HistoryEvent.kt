package com.example.levelup

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HistoryEvent : AppCompatActivity() {
    private lateinit var _adapter : AdapterHistory
    private lateinit var _rvHistory : RecyclerView
    lateinit var db : FirebaseFirestore
    private var _arHistoryEvent = arrayListOf<HistoryEventListItem>()
    private  var _namaEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _locationEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _dateEvent : MutableList<String> = emptyList<String>().toMutableList()

    private fun TampilkanData(){
        _rvHistory.layoutManager = LinearLayoutManager(this )

        val adapterP = AdapterHistory(_arHistoryEvent)
        _rvHistory.adapter = adapterP

        adapterP.setItemClickCallback(object : AdapterHistory.OnItemClickCallback{

            override fun review(position: Int) {
                val intent = Intent(this@HistoryEvent,ReviewEvent::class.java)
                startActivity(intent)
            }
        })
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_event)
        db = FirebaseFirestore.getInstance()

        TampilkanData()

    }
}