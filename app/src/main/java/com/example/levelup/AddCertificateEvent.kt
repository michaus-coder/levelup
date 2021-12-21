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
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*

class AddCertificateEvent : AppCompatActivity() {
    lateinit var _inputFileName : EditText
    lateinit var _uploadBtn : CardView
    lateinit var dbRefrence : FirebaseFirestore
    var storage = FirebaseStorage.getInstance()
    lateinit var sp : SharedPreferences
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
        setContentView(R.layout.activity_add_certificate_event)
        sp = getSharedPreferences("imageData", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("imageData", "")
        editor.apply()
        _inputFileName = findViewById(R.id.editText)
        _uploadBtn = findViewById(R.id.uploadCertifBtn)
//
//        storageRefrence = FirebaseFirestore.getInstance().getRefrence()
//        dbRefrence = FirebaseDatabase.getInstance().getRefrence()
        _inputFileName.setOnClickListener {
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

        _uploadBtn.setOnClickListener{
            val isisp = sp.getString("imageData", null)
            var imageUri : Uri = Uri.parse(isisp)
            if (imageUri != null){
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
                        //TAMBAHKAN FUNCTION BUAT UPLOAD KE FIRESTORE DISINI KALAU MAU
                    } else {
                        Log.d("Status", task.toString())
                    }
                }
            }else{

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
                getFileName(uri)?.let { _inputFileName.setText(it.toString()) }
            }
        }
    }
}