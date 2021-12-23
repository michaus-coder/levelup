package com.example.levelup

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class HistoryEvent : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var _adapter : AdapterHistory
    private lateinit var _rvHistory : RecyclerView
    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    private var _arHistoryEvent = arrayListOf<CreateEventData>()
    private var _arHistory = arrayListOf<HistoryEventListItem>()
    lateinit var navigationView : BottomNavigationView


    private fun readHistory(){
        db.collection("history").whereEqualTo("id_user", auth.currentUser?.email.toString()).get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val historyE = HistoryEventListItem(document.data?.get("id").toString(), document.data?.get("id_event").toString(), document.data?.get("id_user").toString())
                    _arHistory.add(historyE)
                }
                readData(_arHistory)
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "Error getting documents: ", exception)
            }
    }

    private fun readData(arHis : ArrayList<HistoryEventListItem>) {
        for (historyE in arHis){
            db.collection("createEventData").document(historyE.id_event).get()
                .addOnSuccessListener {
                    val eventD = CreateEventData(it.data?.get("id").toString(), it.data?.get("title").toString(), it.data?.get("date").toString(), it.data?.get("time").toString(), it.data?.get("description").toString(), it.data?.get("link").toString(), it.data?.get("category").toString(), it.data?.get("price").toString(), it.data?.get("age").toString(), it.data?.get("location").toString())
                    _arHistoryEvent.add(eventD)
                    TampilkanData()

                }
                .addOnFailureListener { exception ->
                    Log.d("Error", "Error getting documents: ", exception)
                }
        }
    }

    private fun TampilkanData(){
        _rvHistory.layoutManager = LinearLayoutManager(this )

        val adapterP = AdapterHistory(_arHistoryEvent)
        _rvHistory.adapter = adapterP

        adapterP.setItemClickCallback(object : AdapterHistory.OnItemClickCallback{
            override fun review(position: Int) {
                val intent = Intent(this@HistoryEvent,ReviewEvent::class.java)
                intent.putExtra("id_event", _arHistoryEvent[position].ID)
                startActivity(intent)
            }

            override fun certificate(position: Int) {
                val intent = Intent(this@HistoryEvent,Certificate::class.java)
                intent.putExtra("id_event", _arHistoryEvent[position].ID)
                startActivity(intent)
            }
        })
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_event)
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.events
        _rvHistory = findViewById(R.id.rvHistory)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        readHistory()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@HistoryEvent, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.profile -> {
                val intent = Intent(this@HistoryEvent, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.events -> {
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@HistoryEvent, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }
}