package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EventsEvent : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var _adapter : AdapterEvent
    private lateinit var _rvEvents : RecyclerView
    lateinit var db : FirebaseFirestore
    private var _arEvents = arrayListOf<EventDetail>()
    private  var _namaEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _locationEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _dateEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _timeEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _priceEvent : MutableList<String> = emptyList<String>().toMutableList()
    private var auth : FirebaseAuth = Firebase.auth
    lateinit var navigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_event)
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.events
        db = FirebaseFirestore.getInstance()
        _rvEvents = findViewById(R.id.rvEvents)
        TampilkanData()
    }
    private fun TampilkanData(){
        _rvEvents.layoutManager = GridLayoutManager(this,2 )
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

            override fun joinEvent(posisition : Int) {
                val intent = Intent(this@EventsEvent,Payment::class.java)
                intent.putExtra("price", _arEvents[posisition].eventPrice)
                intent.putExtra("id_event", _arEvents[posisition].id)
                startActivity(intent)
            }

        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@EventsEvent, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.profile -> {
                val intent = Intent(this@EventsEvent, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.events -> {
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@EventsEvent, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }
}

