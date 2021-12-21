package com.example.levelup

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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

        adapterZ.setOnItemClickCallback(object : adapterDashboard.OnItemClickCallback {
            override fun onItemClicked(data: CreateEventData) {
                val _btn_delete_event = findViewById<Button>(R.id.btn_delete_dashboard)
            }


            override fun delDataDatabase(position: Int, kirimData: CreateEventData) {
                AlertDialog.Builder(this@Dashboard)
                    .setTitle("HAPUS DATA")
                    .setMessage("Apakah benar data " + "'" + dataArrDashboard[position].title + "'" + " akan DIHAPUS ?")
                    .setPositiveButton(
                        "HAPUS",
                        DialogInterface.OnClickListener { dialog, which ->
                            //buat remove
                            //Log.d("MASUK", dataArrDashboard[position].toString())
                            HapusDataFirebase(db, dataArrDashboard[position].ID)
                        })
                    .setNegativeButton(
                        "BATAL",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@Dashboard,
                                "DATA BATAL DIHAPUS",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    .show()
            }

            override fun editDataDatabse(position: Int, kirimDataEdit: CreateEventData) {
                AlertDialog.Builder(this@Dashboard)
                    .setTitle("EDIT DATA")
                    .setMessage("Apakah benar data " + "'" + dataArrDashboard[position].title + "'" + " akan di EDIT ?")
                    .setPositiveButton(
                        "EDIT",
                        DialogInterface.OnClickListener { dialog, which ->
                            //buat edit
                            //Log.d("MASUK", dataArrDashboard[position].toString())
                            val intent_d = Intent(this@Dashboard, EditEvent::class.java)
                            startActivity(intent_d)
                        })
                    .setNegativeButton(
                        "BATAL",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@Dashboard,
                                "DATA BATAL DIEDIT",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    .show()
            }


        }) //end Callback


    }

    fun readData() {
        db.collection("createEventData").get()
            .addOnSuccessListener { result ->
                dataArrDashboard.clear()
                for (document in result) {
                    val dataBaruDashboard = CreateEventData(
                        document.data.get("id").toString(),
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
                TampilkanData()
                _rv_dashboard.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    fun HapusDataFirebase(db : FirebaseFirestore, ID: String) {
        db.collection("createEventData").document(ID)
            .delete()
            .addOnSuccessListener {
                dataArrDashboard.clear()
                readData()
                //Log.d("MASUK", ID)
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }


}

