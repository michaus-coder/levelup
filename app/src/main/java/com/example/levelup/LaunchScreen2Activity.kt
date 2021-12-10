package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LaunchScreen2Activity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen2)
        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)
        val currentUser = auth.currentUser

        btnGetStarted.setOnClickListener {
            var intent : Intent
            if (currentUser != null){
                intent = Intent(this@LaunchScreen2Activity, MainActivity::class.java)
            }else{
                intent = Intent(this@LaunchScreen2Activity, LoginActivity::class.java)
            }
            startActivity(intent)
        }
    }
}