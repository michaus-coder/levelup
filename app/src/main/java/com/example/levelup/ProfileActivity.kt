package com.example.levelup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val floatingButtonAction = findViewById<FloatingActionButton>(R.id.floatingButtonActionEdit)
        floatingButtonAction.alpha = 0.25f
    }
}