package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val _buttons = findViewById<Button>(R.id.buttons)
//        _buttons.setOnClickListener {
//            val intentbs = Intent(this@MainActivity, Dashboard::class.java)
//            startActivity(intentbs)
//        }
    }
}