package com.example.levelup

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navigationView: BottomNavigationView
    private val floatingButtonAction : FloatingActionButton = findViewById(R.id.floatingButtonActionEdit)
    private val imageProfile : ImageView = findViewById(R.id.imageProfile)
    private val btnNotification : ImageButton = findViewById(R.id.btnNotification)
    private val tvName: TextView = findViewById(R.id.tvName)
    private val tvEmail: TextView = findViewById(R.id.tvEmail)
    private val tvPhone = findViewById<TextView>(R.id.tvPhone)
    private val tvBirthdate = findViewById<TextView>(R.id.tvBirthdate)
    private val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
    private val btnLogout = findViewById<Button>(R.id.btnLogout)
    private val tvJoinedFrom = findViewById<TextView>(R.id.tvJoinedFrom)
    private lateinit var auth : FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        navigationView = findViewById(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.profile
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            updateUI(currentUser)
        }else{

        }
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        floatingButtonAction.setOnClickListener{

        }

        btnNotification.setOnClickListener{

        }

        btnEditProfile.setOnClickListener {

        }

    }

    private fun updateUI(currentUser : FirebaseUser){
        val docRef = db.collection("userdata").document(currentUser.uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            tvName.text = documentSnapshot.data?.get("name").toString()
            tvEmail.text = documentSnapshot.data?.get("email").toString()
            tvPhone.text = documentSnapshot.data?.get("phone").toString()
            tvBirthdate.text = documentSnapshot.data?.get("birthdate").toString()

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.profile -> {
            }
            R.id.events -> {
                val intent = Intent(this@ProfileActivity, EventsEvent::class.java)
                startActivity(intent)
            }
            R.id.dashboard -> {
                val intent = Intent(this@ProfileActivity, Dashboard::class.java)
                startActivity(intent)
            }
            else -> return false
        }
        return true
    }
}