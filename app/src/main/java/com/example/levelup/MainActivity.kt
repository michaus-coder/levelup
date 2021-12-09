package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var navigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.home

//        val _buttons = findViewById<Button>(R.id.buttons)
//        _buttons.setOnClickListener {
//            val intentbs = Intent(this@MainActivity, DetailEvent::class.java)
//            startActivity(intentbs)
//        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                true
            }
            R.id.profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.events -> {
                val intent = Intent(this@MainActivity, EventsEvent::class.java)
                startActivity(intent)
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@MainActivity, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }
}
