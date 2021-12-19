package com.example.levelup

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class EditEvent : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    lateinit var _time : TimePicker
    lateinit var _date : DatePicker
    lateinit var dialog: AlertDialog
    lateinit var layout : LinearLayout
    lateinit var full_date : String
    lateinit var full_time : String

    lateinit var _et_edit_event_title : EditText
    lateinit var _btn_edit_event_setDate : Button
    lateinit var _btn_edit_event_setTime : Button
    lateinit var _et_edit_event_desc : EditText
    lateinit var _et_edit_event_link : EditText
    lateinit var _et_edit_event_price : EditText
    lateinit var _et_edit_event_age : EditText
    lateinit var _spinner_edit_event : EditText
    lateinit var _btn_edit_event_submit : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        db = FirebaseFirestore.getInstance()

        val _et_edit_event_title = findViewById<EditText>(R.id.et_edit_event_title)
        val _btn_edit_event_setDate = findViewById<Button>(R.id.btn_edit_event_setDate)
        val _btn_edit_event_setTime = findViewById<Button>(R.id.btn_edit_event_setTime)
        val _et_edit_event_desc = findViewById<EditText>(R.id.et_edit_event_desc)
        val _et_edit_event_link = findViewById<EditText>(R.id.et_edit_event_link)
        val _et_edit_event_price = findViewById<EditText>(R.id.et_edit_event_price)
        val _et_edit_event_age = findViewById<EditText>(R.id.et_edit_event_age)
        val _spinner_edit_event = findViewById<Spinner>(R.id.spinner_edit_event)
        val _btn_edit_event_submit = findViewById<Button>(R.id.btn_edit_event_submit)

        readData()

        _date = DatePicker(this)
        _time = TimePicker(this)
        dialog = AlertDialog.Builder(this).create()
        layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        _btn_edit_event_setDate.setOnClickListener {
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

                    _btn_edit_event_setDate.text = full_date

                })
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "Close",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    layout.removeView(_date)
                    dialogInterface.cancel()
                })
            dialog.show()
        }

        _btn_edit_event_setTime.setOnClickListener {
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

                    _btn_edit_event_setTime.text = full_time
                })
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "Close",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    layout.removeView(_time)
                    dialogInterface.cancel()
                })
            dialog.show()
        }

        //spinner
        val spinner_data = arrayOf("1", "2", "3")
        val arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinner_data)
        _spinner_edit_event.adapter = arrayAdapter
        _spinner_edit_event.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        //end spinner

    } // end onCreate

    fun readData() {
        db.collection("createEventData").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val dataEdit = CreateEventData(
                        document.data.get("title").toString(),
                        document.data.get("date").toString(),
                        document.data.get("time").toString(),
                        document.data.get("description").toString(),
                        document.data.get("link").toString(),
                        document.data.get("category").toString(),
                        document.data.get("price").toString(),
                        document.data.get("age").toString())
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

}