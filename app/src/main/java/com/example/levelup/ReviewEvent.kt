package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore

class ReviewEvent : AppCompatActivity() {
    lateinit var _eReview : EditText
    lateinit var db : FirebaseFirestore
    private  var _namaEvent : MutableList<String> = emptyList<String>().toMutableList()
    private  var _reviewEvent : MutableList<String> = emptyList<String>().toMutableList()
    lateinit var _btnRev : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_event)
        db= FirebaseFirestore.getInstance()
        _eReview = findViewById(R.id.reviewText)
        _btnRev = findViewById(R.id.submitReviewEvent)
        _btnRev.setOnClickListener{
            TambahData()
            val intent = Intent(this@ReviewEvent,MainActivity::class.java)
            startActivity(intent)
        }

    }
    
    private fun TambahData(){
        for(position in _namaEvent.indices){
            val data = ReviewEventItem(
                _namaEvent[position],
                _reviewEvent[position]
            )
            db.collection("ReviewEvent").document(_namaEvent[position]).set(data)
            //arPahlawan.add(data)
        }

    }

}