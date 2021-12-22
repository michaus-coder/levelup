package com.example.levelup

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EditProfileActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navigationView: BottomNavigationView
    private lateinit var etName : EditText
    private lateinit var etPhone : EditText
    private lateinit var etBirthdate : EditText
    private lateinit var auth : FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        navigationView = findViewById(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.profile
        etBirthdate = findViewById<EditText>(R.id.etBirthdate)
        etName = findViewById<EditText>(R.id.etName)
        etPhone = findViewById<EditText>(R.id.etPhone)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            updateUI(currentUser)
        }else{
            val intent = Intent(this@EditProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        etBirthdate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val monthNum : Int = monthOfYear + 1
                etBirthdate.setText(String.format("%02d-%02d-%02d", year, monthNum, dayOfMonth))
            }, year, month, day)
            dpd.show()
        }

        btnSubmit.setOnClickListener {
            if (etName.text.toString() != "" && etBirthdate.text.toString() != "" && etPhone.text.toString() != ""){
                val docRef = db.collection("userdata").document(auth.currentUser?.email.toString())
                docRef.get().addOnSuccessListener { documentSnapshot ->
                    val newUser = User(auth.currentUser?.email.toString(), etName.text.toString(), auth.currentUser?.email.toString(), etPhone.text.toString(), documentSnapshot.data?.get("password")
                        .toString(),etBirthdate.text.toString(), documentSnapshot.data?.get("profile").toString(), documentSnapshot.data?.get("yearJoined").toString())
                    db.collection("userdata").document(documentSnapshot.id).set(newUser)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile edited successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error edit profile: ${it.toString()}", Toast.LENGTH_SHORT).show()
                        }
                }
            }else{
                Toast.makeText(this, "Input cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateUI(currentUser : FirebaseUser){
        val docRef = db.collection("userdata").document(currentUser.email.toString())
        docRef.get().addOnSuccessListener { documentSnapshot ->
            etName.setText(documentSnapshot.data?.get("name").toString())
            etPhone.setText(documentSnapshot.data?.get("phone").toString())
            etBirthdate.setText(documentSnapshot.data?.get("birthdate").toString())

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.profile -> {
            }
            R.id.events -> {
                val intent = Intent(this@EditProfileActivity, EventsEvent::class.java)
                startActivity(intent)
            }
            R.id.dashboard -> {
                val intent = Intent(this@EditProfileActivity, Dashboard::class.java)
                startActivity(intent)
            }
            else -> return false
        }
        return true
    }
}