package com.example.levelup

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var auth : FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        tvRegister.setTextColor(Color.BLUE)

        if (auth.currentUser != null){
            val intent = Intent(this@LoginActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        tvRegister.setOnClickListener{
            tvRegister.setTextColor(Color.BLACK)
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please insert Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful){
                        return@addOnCompleteListener
                        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed Login: ${it.message}")
                    Toast.makeText(this, "Email / Password incorrect", Toast.LENGTH_SHORT).show()
                }
        }
    }
}