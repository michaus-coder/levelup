package com.example.levelup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore

class AddCertificateEvent : AppCompatActivity() {
    lateinit var _inputFileName : EditText
    lateinit var _uploadBtn : CardView
    lateinit var dbRefrence : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_certificate_event)
        _inputFileName = findViewById(R.id.editText)
        _uploadBtn = findViewById(R.id.uploadCertifBtn)
//
//        storageRefrence = FirebaseFirestore.getInstance().getRefrence()
//        dbRefrence = FirebaseDatabase.getInstance().getRefrence()
    }
}