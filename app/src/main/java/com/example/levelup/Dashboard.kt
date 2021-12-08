package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val _btn_create_event = findViewById<Button>(R.id.btn_create_event)
        _btn_create_event.setOnClickListener {
            val intentbs = Intent(this@Dashboard, CreateEvent::class.java)
            startActivity(intentbs)
        }

    }
}