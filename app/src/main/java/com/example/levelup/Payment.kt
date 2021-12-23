package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Payment : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {
    lateinit var db : FirebaseFirestore
    lateinit var _confirmPaymentBtn : Button
    lateinit var _total : TextView
    private var auth : FirebaseAuth = Firebase.auth
    private lateinit var TempPrice : String
    private lateinit var TempEventId : String
    lateinit var navigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.events
        db = FirebaseFirestore.getInstance()
        _confirmPaymentBtn = findViewById(R.id.buttonPayment)
        TempPrice = intent.getStringExtra("price").toString()
        TempEventId = intent.getStringExtra("id_event").toString()
        _total = findViewById(R.id.totalPrice)

        _total.setText("Rp ." + TempPrice)

        _confirmPaymentBtn.setOnClickListener{
            val dbRef = db.collection("history").document()
            val History = HistoryEventListItem(
                dbRef.id,
                TempEventId,
                auth.currentUser!!?.email.toString()

            )
            dbRef.set(History)
                .addOnSuccessListener {
                    Log.d("Firebase", "Transaction is successfully added")
                    val intent = Intent(this@Payment, HistoryEvent::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@Payment, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.profile -> {
                val intent = Intent(this@Payment, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.events -> {
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@Payment, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }

}