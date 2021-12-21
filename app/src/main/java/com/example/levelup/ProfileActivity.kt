package com.example.levelup

import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    private lateinit var navigationView: BottomNavigationView
    private lateinit var auth : FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var fab : FloatingActionButton
    private lateinit var imageProfile : ImageView
    private lateinit var tvName : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvPhone : TextView
    private lateinit var tvBirthdate : TextView
    private lateinit var btnEditProfile : Button
    private lateinit var btnLogout : Button
    private lateinit var tvJoinedFrom : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        navigationView = findViewById(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.profile
        fab = findViewById<FloatingActionButton>(R.id.fabEdit)
        imageProfile = findViewById<ImageView>(R.id.imageProfile)
        tvName = findViewById<TextView>(R.id.tvName)
        tvEmail = findViewById<TextView>(R.id.tvEmail)
        tvPhone = findViewById<TextView>(R.id.tvPhone)
        tvBirthdate = findViewById<TextView>(R.id.tvBirthdate)
        btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
        btnLogout = findViewById<Button>(R.id.btnLogout)
        tvJoinedFrom = findViewById<TextView>(R.id.tvJoinedFrom)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            updateUI(currentUser)
        }else{
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener{

        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val currentUser = auth.currentUser
        if (currentUser != null){
            updateUI(currentUser)
        }else{
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(currentUser : FirebaseUser){
        val docRef = db.collection("userdata").document(currentUser.email.toString())
        docRef.get().addOnSuccessListener { documentSnapshot ->
            tvName.text = documentSnapshot.data?.get("name").toString()
            tvEmail.text = documentSnapshot.data?.get("email").toString()
            tvPhone.text = documentSnapshot.data?.get("phone").toString()
            tvBirthdate.text = documentSnapshot.data?.get("birthdate").toString()
            val joined : String = "Joined From " + documentSnapshot.data?.get("yearJoined").toString()
            tvJoinedFrom.text = joined
            val context = this
            val imageRes = context.resources.getIdentifier(documentSnapshot.data?.get("profile").toString(), "drawable", context.packageName)
            Picasso.with(context)
                .load(documentSnapshot.data?.get("profile").toString())
                .placeholder(R.drawable.profile)
                .into(imageProfile)
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