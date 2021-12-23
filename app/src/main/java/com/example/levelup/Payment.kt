package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Payment : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    lateinit var _confirmPaymentBtn : Button
    lateinit var _total : TextView
    private var auth : FirebaseAuth = Firebase.auth
    private lateinit var TempPrice : String
    private lateinit var TempEventId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        db = FirebaseFirestore.getInstance()
        _confirmPaymentBtn = findViewById(R.id.buttonPayment)
        TempPrice = intent.getStringExtra("price").toString()
        TempEventId = intent.getStringExtra("id_event").toString()
        _total = findViewById(R.id.totalPrice)

        _total.setText(TempPrice)

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
}