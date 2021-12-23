package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ReviewEvent : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var _eReview : EditText
    lateinit var db : FirebaseFirestore
    private  var _namaEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _reviewEvent : MutableList<String> = emptyList<String>().toMutableList()
    lateinit var _btnRev : CardView
    lateinit var navigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_event)
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.events
        val id_event = intent.getStringExtra("id_event")
        db = FirebaseFirestore.getInstance()
        _eReview = findViewById(R.id.reviewText)
        _btnRev = findViewById(R.id.submitReviewEvent)
        _btnRev.setOnClickListener{
            if (id_event != null && _eReview.text.toString() != "") {
                TambahData(id_event, _eReview.text.toString())
            }else{
                Toast.makeText(this, "Review cannot be blank", Toast.LENGTH_SHORT).show()
            }

        }

    }
    
    private fun TambahData(ie : String, review : String){
        val ri = ReviewEventItem(ie, review)
        db.collection("eventReviews").add(ri)
            .addOnSuccessListener {
                Toast.makeText(this, "Review successfully added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ReviewEvent,MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("error", it.toString())
            }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@ReviewEvent, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.profile -> {
                val intent = Intent(this@ReviewEvent, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.events -> {
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@ReviewEvent, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }

}