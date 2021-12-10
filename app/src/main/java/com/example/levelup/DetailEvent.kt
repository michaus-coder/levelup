package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class DetailEvent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        val _et_detail_event = findViewById<EditText>(R.id.et_detail_event)
        val _btn_detail_event_certificate = findViewById<Button>(R.id.btn_detail_event_certificate)
        val _btn_detail_event_reminder = findViewById<Button>(R.id.btn_detail_event_reminder)

        _btn_detail_event_certificate.setOnClickListener {
            val intent_de = Intent(this@DetailEvent, AddCertificateEvent::class.java)
            startActivity(intent_de)
        }

//        _btn_detail_event_reminder.setOnClickListener {
//            val intentbs = Intent(this@DetailEvent, DetailEvent::class.java)
//            startActivity(intentbs)
//        }

    }
}