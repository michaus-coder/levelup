package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class EventsEvent : AppCompatActivity() {
    private lateinit var _adapter : AdapterEvent
    private lateinit var _rvEvents : RecyclerView
    lateinit var db : FirebaseFirestore
    private var _arEvents = arrayListOf<EventDetail>()
    private  var _namaEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _locationEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _dateEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _timeEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _priceEvent : MutableList<String> = emptyList<String>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_event)
        db = FirebaseFirestore.getInstance()
        _rvEvents = findViewById(R.id.rvEvents)
        TampilkanData()
    }
    private fun TampilkanData(){
        _rvEvents.layoutManager = LinearLayoutManager(this )
        val adapterP = AdapterEvent(_arEvents)
        _rvEvents.adapter = adapterP

        db.collection("createEventData")
            .get()
            .addOnSuccessListener { result ->
                _arEvents.clear()
                for(document in result) {
                    val data = EventDetail(document.id,
                        document.data.get("title").toString(),
                        document.data.get("location").toString(),
                        document.data.get("date").toString(),
                        document.data.get("time").toString(),
                        document.data.get("price").toString())
                    _arEvents.add(data)
                }
                adapterP.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }

        adapterP.setItemClickCallback(object : AdapterEvent.OnItemClickCallback{
            override fun detailEvent() {
                val intent = Intent(this@EventsEvent,DetailEvent::class.java)
                startActivity(intent)
            }

            override fun joinEvent() {
//                val dbRef = db.collection("history").document()
//                val History = HistoryEventListItem(
//                    dbRef.id,
//                    _namaEvent.toString(),
//                    _locationEvent.toString(),
//                    _dateEvent.toString(),
////                    id_user = nanti id user disini
//                )
//                dbRef.set(History)
//                    .addOnSuccessListener {
//                        Log.d("Firebase", "Transaction is successfully added")
//                    }
//                    .addOnFailureListener {
//                        Log.d("Firebase", it.message.toString())
//                    }
            }

        })
    }
}

