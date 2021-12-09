package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LaunchScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@LaunchScreenActivity, LaunchScreen2Activity::class.java)
            startActivity(intent)
        }, 3000)
    }
}