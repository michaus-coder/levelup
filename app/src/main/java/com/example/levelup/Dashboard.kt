package com.example.levelup

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Dashboard : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var db : FirebaseFirestore
    private lateinit var _rv_dashboard : RecyclerView
    var dataArrDashboard : ArrayList<CreateEventData> = ArrayList<CreateEventData>()

    lateinit var navigationView : BottomNavigationView
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.dashboard

        _rv_dashboard = findViewById(R.id.rv_dashboard)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        readData()
        TampilkanData()

        val _btn_create_event = findViewById<Button>(R.id.btn_create_event)
        _btn_create_event.setOnClickListener {
            val intent_d = Intent(this@Dashboard, CreateEvent::class.java)
            startActivity(intent_d)
        }

        val _btn_history_join_event = findViewById<ImageButton>(R.id.btn_history_join_event)
        _btn_history_join_event.setOnClickListener {
            val intent_d = Intent(this@Dashboard, HistoryEvent::class.java)
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
                            intent_d.putExtra("ID Kirim",dataArrDashboard[position].ID)
                            Log.d("ID Kirim", dataArrDashboard[position].ID.toString())
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

            override fun detailDatabase(position: Int, kirimDataDetail: CreateEventData) {
                AlertDialog.Builder(this@Dashboard)
                    .setTitle("Show Detail DATA")
                    //.setMessage("Apakah benar data " + "'" + dataArrDashboard[position].title + "'" + " akan di EDIT ?")
                    .setPositiveButton(
                        "GO TO DETAIL",
                        DialogInterface.OnClickListener { dialog, which ->
                            //buat detail
                            val intent_d = Intent(this@Dashboard, DetailEvent::class.java)
                            intent_d.putExtra("ID Kirim",dataArrDashboard[position].ID)
                            Log.d("ID Kirim", dataArrDashboard[position].ID.toString())
                            startActivity(intent_d)
                        })
                    .setNegativeButton(
                        "BATAL",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@Dashboard,
                                "Cancel",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    .show()
            }


        }) //end Callback

    }

    fun readData() {
        db.collection("createEventData").whereEqualTo("id_user", auth.currentUser!!?.email.toString()).get()
            .addOnSuccessListener { result ->
                dataArrDashboard.clear()
                for (document in result) {
                    val dataBaruDashboard = CreateEventData(
                        document.data.get("id_user").toString(),
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@Dashboard, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.profile -> {
                val intent = Intent(this@Dashboard, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.events -> {
                val intent = Intent(this@Dashboard, EventsEvent::class.java)
                startActivity(intent)

            }
            R.id.dashboard -> {

            }
            else -> return false
        }
        return true
    }


}

