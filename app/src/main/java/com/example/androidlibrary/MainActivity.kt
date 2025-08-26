package com.example.androidlibrary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.photocap.CaptureCamera

class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            CaptureCamera.openCamera(this, CAMERA_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST) {
            val bitmap = CaptureCamera.handleResult(this, resultCode)
            if (bitmap != null) {
                findViewById<ImageView>(R.id.image).setImageBitmap(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CaptureCamera.onRequestPermissionsResult(this, requestCode, grantResults)
    }
}
