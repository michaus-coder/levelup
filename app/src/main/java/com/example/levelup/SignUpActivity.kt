package com.example.levelup

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import java.io.File
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private var auth : FirebaseAuth = Firebase.auth
    val MONTHS : ArrayList<String> = arrayListOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private lateinit var etProfile : EditText
    lateinit var sp : SharedPreferences
    var storage = FirebaseStorage.getInstance()
    val db = Firebase.firestore
    // companion object
    companion object{
        const val IMAGE_CHOOSE = 1000
        const val PERMISSION_CODE = 1001

        /* get actual file name or extension */
        fun Context.getFileExtension(uri: Uri): String? = when (uri.scheme) {
            // get file extension
            ContentResolver.SCHEME_FILE -> File(uri.path!!).extension
            // get actual name of file
            //ContentResolver.SCHEME_FILE -> File(uri.path!!).name
            ContentResolver.SCHEME_CONTENT -> getCursorContent(uri)
            else -> null
        }

        private fun Context.getCursorContent(uri: Uri): String? = try {
            contentResolver.query(uri, null, null, null, null)?.let { cursor ->
                cursor.run {
                    val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
                    if (moveToFirst()) mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
                    // case for get actual name of file
                    //if (moveToFirst()) getString(getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    else null
                }.also { cursor.close() }
            }
        } catch (e: Exception) {
            null
        }
    }
    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        sp = getSharedPreferences("imageData", Context.MODE_PRIVATE)
        etProfile = findViewById<EditText>(R.id.etPhoto)
        val editor = sp.edit()
        editor.putString("imageData", "")
        editor.apply()
        val etBirthdate = findViewById<EditText>(R.id.etBirthdate)
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etPasswordConfirm = findViewById<EditText>(R.id.etPasswordConfirm)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
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
        etProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                }else {
                    chooseImageGallery()
                }
            }else{
                chooseImageGallery()
            }
        }

        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            if (etName.text.toString() == "" || etEmail.text.toString() == "" || etPassword.text.toString() == "" || etPasswordConfirm.text.toString() == "" || etBirthdate.text.toString() == ""){
                Toast.makeText(this, "All inputs cannot blank", Toast.LENGTH_SHORT).show()
            }else if(etPassword.text.toString() != etPasswordConfirm.text.toString()){
                Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show()
            }else{
                val isisp = sp.getString("imageData", null)
                var imageUri : Uri = Uri.parse(isisp)
                if (imageUri.toString() == ""){
                    auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                // Sign in success, update UI with the signed-in user's information
                                val downloadUri : String = "images/defaultuser.jpg"
                                Log.d("Status", "createUserWithEmail:success")
                                var uid : String = ""
                                val user = auth.currentUser
                                user?.let {
                                    for (profile in it.providerData) {
                                        uid = profile.uid
                                    }
                                }
                                val newUser : User = User(uid, etName.text.toString(), etEmail.text.toString(), etPhone.text.toString(), etPassword.text.toString(), etBirthdate.text.toString(), downloadUri)
                                db.collection("userdata")
                                    .document(uid)
                                    .set(newUser)
                                    .addOnSuccessListener {
                                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Log.d("Error", "Error adding data: ${it.toString()}")
                                        Toast.makeText(this, "Error adding data to database", Toast.LENGTH_SHORT)
                                    }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Status", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }else{
                    val pd : ProgressDialog = ProgressDialog(this)
                    pd.setTitle("Uploading...")
                    pd.show()
                    Log.d("StatusUpload", "Uploading...")
                    var ref : StorageReference = storage.reference.child("images/" + UUID.randomUUID().toString())
                    var uploadTask = ref.putFile(imageUri)
                        .addOnSuccessListener(
                            OnSuccessListener<UploadTask.TaskSnapshot?> {
                                // Dismiss dialog
                                pd.dismiss()
                                Toast
                                    .makeText(
                                        this,
                                        "Image Uploaded!!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()

                            })
                        .addOnFailureListener(OnFailureListener { e -> // Error, Image not uploaded
                            pd.dismiss()
                            Toast
                                .makeText(
                                    this,
                                    "Failed " + e.message,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        })
                        .addOnProgressListener { taskSnapshot ->
                            // Progress Listener for loading
                            // percentage on the dialog box
                            val progress = (100.0
                                    * taskSnapshot.bytesTransferred
                                    / taskSnapshot.totalByteCount)
                            pd.setMessage(
                                "Uploaded "
                                        + progress.toInt() + "%"
                            )
                        }
                    val urlTask = uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        ref.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                                .addOnCompleteListener {
                                    if (task.isSuccessful){
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Status", "createUserWithEmail:success")
                                        var uid : String = ""
                                        val user = auth.currentUser
                                        user?.let {
                                            for (profile in it.providerData) {
                                                uid = profile.uid
                                            }
                                        }
                                        val newUser : User = User(uid, etName.text.toString(), etEmail.text.toString(), etPhone.text.toString(), etPassword.text.toString(), etBirthdate.text.toString(), downloadUri.toString())
                                        db.collection("userdata")
                                            .document(uid)
                                            .set(newUser)
                                            .addOnSuccessListener {
                                                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                                startActivity(intent)
                                            }
                                            .addOnFailureListener {
                                                Log.d("Error", "Error adding data: ${it.toString()}")
                                                Toast.makeText(this, "Error adding data to database", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Status", "createUserWithEmail:failure", task.exception)
                                        Toast.makeText(baseContext, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Log.d("Status", task.toString())
                        }
                    }
                }
            }
        }

    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Image", data.toString())
        if (data != null) {
            data.data?.also { uri ->
                val editor = sp.edit()
                editor.putString("imageData", uri.toString())
                editor.apply()
                getFileName(uri)?.let { etProfile.setText(it.toString()) }
            }
        }
    }
}
