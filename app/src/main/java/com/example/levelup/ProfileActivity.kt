package com.example.levelup

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

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
    lateinit var sp : SharedPreferences
    private var storage = FirebaseStorage.getInstance()
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, SignUpActivity.PERMISSION_CODE)
                }else {
                    chooseImageGallery()
                }
            }else{
                chooseImageGallery()
            }
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
            Picasso.get().load(documentSnapshot.data?.get("profile").toString()).into(imageProfile)
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
                val pd : ProgressDialog = ProgressDialog(this)
                pd.setTitle("Uploading...")
                pd.show()
                Log.d("StatusUpload", "Uploading...")
                var ref : StorageReference = storage.reference.child("images/" + UUID.randomUUID().toString())
                var uploadTask = ref.putFile(uri)
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
                        val docRef = db.collection("userdata").document(auth.currentUser?.email.toString())
                        docRef.get().addOnSuccessListener { documentSnapshot ->
                            val newUser : User = User(
                                auth.currentUser?.email.toString(), documentSnapshot.data?.get("name").toString(), auth.currentUser?.email.toString(), documentSnapshot.data?.get("phone").toString(), documentSnapshot.data?.get("password").toString(), documentSnapshot.data?.get("birthdate").toString(), downloadUri.toString(), documentSnapshot.data?.get("yearJoined").toString())
                            db.collection("userdata")
                                .document(auth.currentUser?.email.toString())
                                .set(newUser)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Image profile updated successfully", Toast.LENGTH_SHORT).show()
                                    auth.currentUser?.let { it1 -> updateUI(it1) }
                                }
                                .addOnFailureListener {
                                    Log.d("Error", "Error adding data: ${it.toString()}")
                                    Toast.makeText(this, "Error adding data to database", Toast.LENGTH_SHORT).show()
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