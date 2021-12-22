package com.example.levelup

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.autofill.AutofillValue
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast




class EditEvent : AppCompatActivity() {
    var dataArrDashboard : ArrayList<CreateEventData> = ArrayList<CreateEventData>()

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
    lateinit var _et_edit_event_location : EditText
    lateinit var _spinner_edit_event : Spinner
    lateinit var _btn_edit_event_submit : Button
    lateinit var temp_id_terima : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        db = FirebaseFirestore.getInstance()

        _et_edit_event_title = findViewById(R.id.et_edit_event_title)
        _btn_edit_event_setDate = findViewById(R.id.btn_edit_event_setDate)
        _btn_edit_event_setTime = findViewById(R.id.btn_edit_event_setTime)
        _et_edit_event_desc = findViewById(R.id.et_edit_event_desc)
        _et_edit_event_link = findViewById(R.id.et_edit_event_link)
        _et_edit_event_price = findViewById(R.id.et_edit_event_price)
        _et_edit_event_age = findViewById(R.id.et_edit_event_age)
        _et_edit_event_location = findViewById(R.id.et_edit_event_location)
        _spinner_edit_event = findViewById(R.id.spinner_edit_event)
        _btn_edit_event_submit = findViewById(R.id.btn_edit_event_submit)


        val temp_ID = intent.getStringExtra("ID Kirim")
        Log.d("ID Kirim", temp_ID.toString())

        temp_id_terima = temp_ID.toString()

        val myToast = Toast.makeText(applicationContext,"Don't forget to recheck the CATEGORY!", Toast.LENGTH_LONG)
        myToast.setGravity(Gravity.LEFT,160,0)

        readData()
        myToast.show()
        //Toast.makeText(this, "Don't forget to recheck the CATEGORY!", Toast.LENGTH_LONG).show()
        updateData()

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
        val spinner_data = arrayOf("Birthday", "Meeting", "Gabut")
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

        _btn_edit_event_submit.setOnClickListener {
                val docRef = db.collection("createEventData").document(temp_id_terima)
                docRef.get().addOnSuccessListener {
                    val newEditData = CreateEventData(
                        temp_id_terima,
                        _et_edit_event_title.text.toString(),
                        full_date,
                        full_time,
                        _et_edit_event_desc.text.toString(),
                        _et_edit_event_link.text.toString(),
                        _spinner_edit_event.selectedItem.toString(),
                        _et_edit_event_price.text.toString(),
                        _et_edit_event_age.text.toString(),
                        _et_edit_event_location.text.toString())

                    db.collection("createEventData").document(temp_id_terima).set(newEditData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Event edited successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@EditEvent, Dashboard::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error Edit Event: ${it.toString()}", Toast.LENGTH_SHORT).show()
                        }
                }
        }

    } // end onCreate

    fun readData() {
        db.collection("createEventData").get()
            .addOnSuccessListener { result ->
                dataArrDashboard.clear()
                for (document in result) {
                    val dataBaruDashboard = CreateEventData(
                        document.data.get("id").toString(),
                        document.data.get("title").toString(),
                        document.data.get("date").toString(),
                        document.data.get("time").toString(),
                        document.data.get("description").toString(),
                        document.data.get("link").toString(),
                        document.data.get("category").toString(),
                        document.data.get("price").toString(),
                        document.data.get("age").toString(),
                        document.data.get("location").toString() )
                    dataArrDashboard.add(dataBaruDashboard)
                }
                //TampilkanData()
                //_rv_dashboard.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    fun updateData(){
        val docRef = db.collection("createEventData").document(temp_id_terima)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            _et_edit_event_title.setText(documentSnapshot.data?.get("title").toString())
            _et_edit_event_desc.setText(documentSnapshot.data?.get("description").toString())
            _et_edit_event_link.setText(documentSnapshot.data?.get("link").toString())
            _et_edit_event_price.setText(documentSnapshot.data?.get("price").toString())
            _et_edit_event_age.setText(documentSnapshot.data?.get("age").toString())
            _et_edit_event_location.setText(documentSnapshot.data?.get("location").toString())
            _btn_edit_event_setDate.setText(documentSnapshot.data?.get("date").toString())
            _btn_edit_event_setTime.setText(documentSnapshot.data?.get("time").toString())
            full_date = documentSnapshot.data?.get("date").toString()
            full_time = documentSnapshot.data?.get("time").toString()
        }
    }


//    fun readData() {
//        val docref = db.collection("createEventData").document("x5UCNiqPRvJNKWm90xle")
//        docref.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    _et_edit_event_title.text =
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }
//    }

//    fun ini(db : FirebaseFirestore, ID: String) {
//        db.collection("createEventData").document(ID)
//            .get()
//            .addOnSuccessListener {
//                dataArrDashboard.clear()
//                readData()
//                //Log.d("MASUK", ID)
//            }
//            .addOnFailureListener {
//                Log.d("Firebase", it.message.toString())
//            }
//    }



}