package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class DetailEvent : AppCompatActivity() {
    var dataArrDashboard : ArrayList<CreateEventData> = ArrayList<CreateEventData>()

    lateinit var _tv_detailEvent_title : TextView
    lateinit var _tv_detailEvent_time : TextView
    lateinit var _tv_detailEvent_date : TextView
    lateinit var _tv_detailEvent_desc : TextView
    lateinit var _tv_detailEvent_link : TextView
    lateinit var _tv_detailEvent_location : TextView
    lateinit var _tv_detailEvent_category : TextView
    lateinit var _tv_detailEvent_price : TextView
    lateinit var _tv_detailEvent_age : TextView
    lateinit var _btn_detail_event_certificate : Button

    lateinit var db : FirebaseFirestore

    lateinit var temp_id_terima : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        _tv_detailEvent_title = findViewById(R.id.tv_detailEvent_title)
        _tv_detailEvent_time = findViewById(R.id.tv_detailEvent_time)
        _tv_detailEvent_date = findViewById(R.id.tv_detailEvent_date)
        _tv_detailEvent_desc = findViewById(R.id.tv_detailEvent_desc)
        _tv_detailEvent_link = findViewById(R.id.tv_detailEvent_link)
        _tv_detailEvent_location = findViewById(R.id.tv_detailEvent_location)
        _tv_detailEvent_category = findViewById(R.id.tv_detailEvent_category)
        _tv_detailEvent_price = findViewById(R.id.tv_detailEvent_price)
        _tv_detailEvent_age = findViewById(R.id.tv_detailEvent_age)

        _btn_detail_event_certificate = findViewById(R.id.btn_detail_event_certificate)

        db = FirebaseFirestore.getInstance()

        val temp_ID = intent.getStringExtra("ID Kirim")
        Log.d("ID Kirim", temp_ID.toString())

        temp_id_terima = temp_ID.toString()

        readData()
        updateData()

        _btn_detail_event_certificate.setOnClickListener {
            val intent_de = Intent(this@DetailEvent, AddCertificateEvent::class.java)
            startActivity(intent_de)
        }


    }

    fun readData() {
        db.collection("createEventData").get()
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
                //TampilkanData()
                //_rv_dashboard.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    fun updateData(){
        val docRef = db.collection("createEventData").document(temp_id_terima)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            _tv_detailEvent_title.setText(documentSnapshot.data?.get("title").toString())
            _tv_detailEvent_time.setText(documentSnapshot.data?.get("time").toString())
            _tv_detailEvent_date.setText(documentSnapshot.data?.get("date").toString())
            _tv_detailEvent_desc.setText(documentSnapshot.data?.get("description").toString())
            _tv_detailEvent_link.setText(documentSnapshot.data?.get("link").toString())
            _tv_detailEvent_location.setText(documentSnapshot.data?.get("location").toString())
            _tv_detailEvent_category.setText(documentSnapshot.data?.get("category").toString())
            _tv_detailEvent_price.setText(documentSnapshot.data?.get("price").toString())
            _tv_detailEvent_age.setText(documentSnapshot.data?.get("age").toString())
        }
    }
}