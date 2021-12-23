package com.example.levelup

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import android.graphics.Bitmap

import android.graphics.BitmapFactory

import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor

import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.InputStream
import java.lang.Exception
import java.net.URL


class Certificate : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var navigationView : BottomNavigationView
    lateinit var db : FirebaseFirestore
    lateinit var imgURL : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)
        db = FirebaseFirestore.getInstance()
        val certImg = findViewById<ImageView>(R.id.imgCertificate)
        val id_event = intent.getStringExtra("id_event")
        val btnDownload = findViewById<Button>(R.id.btnDownload)
        btnDownload.visibility = View.INVISIBLE
        imgURL = ""
        CoroutineScope(Dispatchers.IO).async{
            if (id_event != null) {
                db.collection("eventCertificates").document(id_event).get()
                    .addOnSuccessListener { documentSnapshot ->
                        Picasso.get().load(documentSnapshot.data?.get("certificate").toString()).into(certImg)
                        imgURL = documentSnapshot.data?.get("certificate").toString()
                        btnDownload.visibility = View.VISIBLE
                    }
                    .addOnFailureListener {
                        Log.d("error", it.toString())
                    }
            }
        }
        btnDownload.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save an image
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                askPermissions()
            } else {
                downloadImage(imgURL)
            }
        }
        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.events
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                val intent = Intent(this@Certificate, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.profile -> {
                val intent = Intent(this@Certificate, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.events -> {
                true
            }
            R.id.dashboard -> {
                val intent = Intent(this@Certificate, Dashboard::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            downloadImage(imgURL)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    downloadImage(imgURL)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    private var msg: String? = ""
    private var lastMsg = ""

    @SuppressLint("Range")
    private fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("certificate.jpg")
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    "certificate.jpg"
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + "certificate.jpg"
            else -> "There's nothing to download"
        }
        return msg
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

}