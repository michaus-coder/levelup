package com.example.levelup

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreateEvent : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    lateinit var _time : TimePicker
    lateinit var _date : DatePicker
    lateinit var dialog: AlertDialog
    lateinit var layout : LinearLayout
    lateinit var full_date : String
    lateinit var full_time : String

    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val _et_create_event_title = findViewById<EditText>(R.id.et_create_event_title)
        val _btn_create_event_setDate = findViewById<Button>(R.id.btn_create_event_setDate)
        val _btn_create_event_setTime = findViewById<Button>(R.id.btn_create_event_setTime)
        val _et_create_event_desc = findViewById<EditText>(R.id.et_create_event_desc)
        val _et_create_event_link = findViewById<EditText>(R.id.et_create_event_link)
        val _et_create_event_price = findViewById<EditText>(R.id.et_create_event_price)
        val _et_create_event_age = findViewById<EditText>(R.id.et_create_event_age)
        val _et_create_event_location = findViewById<EditText>(R.id.et_create_event_location)
        val _spinner_create_event = findViewById<Spinner>(R.id.spinner_create_event)
        val _btn_create_event_submit = findViewById<Button>(R.id.btn_create_event_submit)

        _date = DatePicker(this)
        _time = TimePicker(this)
        dialog = AlertDialog.Builder(this).create()
        layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL


        _btn_create_event_setDate.setOnClickListener {
            layout.addView(_date)
            dialog.setView(layout)
            dialog.setButton(
                DialogInterface.BUTTON_POSITIVE, "Submit",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    layout.removeView(_date)

                    //Date
                    var day = _date.dayOfMonth.toString()
                    var month = (_date.month + 1).toString()
                    var year = _date.year.toString()
                    full_date = day + "-" + month + "-" + year

                    _btn_create_event_setDate.text = full_date

                })
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "Close",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    layout.removeView(_date)
                    dialogInterface.cancel()
                })
            dialog.show()
        }

        _btn_create_event_setTime.setOnClickListener {
            layout.addView(_time)
            dialog.setView(layout)
            dialog.setButton(
                DialogInterface.BUTTON_POSITIVE, "Submit",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    layout.removeView(_time)

                    //Time
                    var menit = _time.minute.toString()
                    var jam = _time.hour.toString()
                    full_time = jam + " : " + menit

                    _btn_create_event_setTime.text = full_time
                })
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "Close",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    layout.removeView(_time)
                    dialogInterface.cancel()
                })
            dialog.show()
        }

        _btn_create_event_submit.setOnClickListener {

            val dataBaru = CreateEventData(
                auth.currentUser!!?.email.toString(),
                UUID.randomUUID().toString(),
                _et_create_event_title.text.toString(),
                full_date,
                full_time,
                _et_create_event_desc.text.toString(),
                _et_create_event_link.text.toString(),
                _spinner_create_event.selectedItem.toString(),
                _et_create_event_price.text.toString(),
                _et_create_event_age.text.toString(),
                _et_create_event_location.text.toString()
            )

            db.collection("createEventData").document(dataBaru.ID)
                .set(dataBaru)
                .addOnSuccessListener {
                    Log.d("Firebase", "Simpan Data Berhasil")
                    Log.d("ID", dataBaru.ID)
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }

            val intent = Intent(this@CreateEvent, Dashboard::class.java)
            startActivity(intent)

        }

        //Spinner
        val spinner_data = arrayOf("Birthday", "Meeting", "Economy", "Art", "Law", "Psychlogy", "Science", "Sport", "Music", "Politics", "Acedemic", "Health", "Technology", "Business")
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