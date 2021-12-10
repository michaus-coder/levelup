package com.example.levelup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class CreateEvent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        val _et_create_event_title = findViewById<EditText>(R.id.et_create_event_title)
        val _et_create_event_date = findViewById<EditText>(R.id.et_create_event_date)
        val _et_create_event_desc = findViewById<EditText>(R.id.et_create_event_desc)
        val _et_create_event_link = findViewById<EditText>(R.id.et_create_event_link)
        val _et_create_event_price = findViewById<EditText>(R.id.et_create_event_price)
        val _et_create_event_age = findViewById<EditText>(R.id.et_create_event_age)
        val _btn_create_event_submit = findViewById<Button>(R.id.btn_create_event_submit)


        _btn_create_event_submit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("eventTitle", _et_create_event_title.text.toString())
            bundle.putString("eventDate", _et_create_event_date.text.toString())
            bundle.putString("eventDesc", _et_create_event_desc.text.toString())
            bundle.putString("eventLink", _et_create_event_link.text.toString())
            bundle.putString("eventPrice", _et_create_event_price.text.toString())
            bundle.putString("eventAge", _et_create_event_age.text.toString())

            val intent = Intent(this, EventsEvent::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        //Spinner
        val spinner_data = arrayOf("1", "2", "3")
        val _spinner_create_event = findViewById<Spinner>(R.id.spinner_create_event)
        val arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinner_data)
        _spinner_create_event.adapter = arrayAdapter
        _spinner_create_event.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        //End of spinner


    }
}