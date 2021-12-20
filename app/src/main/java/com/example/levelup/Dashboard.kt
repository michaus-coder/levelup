package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Dashboard : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var _rv_dashboard : RecyclerView
    var dataArrDashboard : ArrayList<CreateEventData> = ArrayList<CreateEventData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        _rv_dashboard = findViewById(R.id.rv_dashboard)

        db = FirebaseFirestore.getInstance()

        readData()
        TampilkanData()



        val _btn_create_event = findViewById<Button>(R.id.btn_create_event)
        _btn_create_event.setOnClickListener {
            val intent_d = Intent(this@Dashboard, CreateEvent::class.java)
            startActivity(intent_d)
        }

    }

    private fun TampilkanData() {
        _rv_dashboard.layoutManager = LinearLayoutManager(this)

        val adapterZ = adapterDashboard(dataArrDashboard)
        _rv_dashboard.adapter = adapterZ
    }

    fun readData() {
        db.collection("createEventData").get()
            .addOnSuccessListener { result ->
                dataArrDashboard.clear()
                for (document in result) {
                    val dataBaruDashboard = CreateEventData(
                        document.data.get("title").toString(),
                        document.data.get("date").toString(),
                        document.data.get("time").toString(),
                        document.data.get("description").toString(),
                        document.data.get("link").toString(),
                        document.data.get("category").toString(),
                        document.data.get("price").toString(),
                        document.data.get("age").toString(),
                        document.data.get("location").toString() )
                    dataArrDashboard.add(dataBaruDashboard)
                }
                _rv_dashboard.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

}