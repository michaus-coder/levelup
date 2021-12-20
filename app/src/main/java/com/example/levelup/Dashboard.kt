package com.example.levelup

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

        val _btn_delete_dashboard = findViewById<Button>(R.id.btn_delete_dashboard)
        _btn_delete_dashboard.setOnClickListener {

        }

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

//    fun HapusDataFirebase() {
//        db.collection("cities").document()
//            .delete()
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
//    }

//    fun deleteFirebase(db : FirebaseFirestore, title: String) {
//        db.collection("createEventData").document(title)
//            .delete()
//            .addOnSuccessListener {
//                readData()
//            }
//            .addOnFailureListener {
//                Log.d("Firebase", it.message.toString())
//            }
//    }

}

