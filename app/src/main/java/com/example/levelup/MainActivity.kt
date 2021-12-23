package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var navigationView : BottomNavigationView
    private lateinit var _rvHomeEvents : RecyclerView
    lateinit var db : FirebaseFirestore
    private var _arEvents = arrayListOf<EventDetail>()
    private var auth : FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.home
        db = FirebaseFirestore.getInstance()
        _rvHomeEvents = findViewById(R.id.rvhomeevents)
        TampilkanData()
    }
    private fun TampilkanData(){
        _rvHomeEvents.layoutManager = GridLayoutManager(this,2 )
        val adapterP = AdapterEvent(_arEvents)
        _rvHomeEvents.adapter = adapterP

        db.collection("createEventData").whereNotEqualTo("id_user",auth.currentUser!!?.email.toString()).limit(4)
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
                val intent = Intent(this@MainActivity,Payment::class.java)
                intent.putExtra("price", _arEvents[posisition].eventPrice)
                intent.putExtra("id_event", _arEvents[posisition].id)
                startActivity(intent)
            }

        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                true
            }
            R.id.profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.events -> {
                val intent = Intent(this@MainActivity, EventsEvent::class.java)
                startActivity(intent)
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@MainActivity, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }
}
