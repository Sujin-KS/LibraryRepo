package com.example.photocap



import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.core.graphics.createBitmap



object CaptureCamera {
    private var photoUri: Uri? = null
    private lateinit var photoFile: File
    private const val CAMERA_PERMISSION_CODE = 101



    fun openCamera(activity: Activity, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            launchCamera(activity, requestCode)
        }
    }

    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            launchCamera(activity, CAMERA_PERMISSION_CODE)
        }
    }



    private fun launchCamera(activity: Activity, requestCode: Int) {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = File(activity.getExternalFilesDir(null), "photo.jpg")
        photoUri = FileProvider.getUriForFile(
            activity,
            activity.packageName + ".fileprovider",
            photoFile
        )

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
        activity.startActivityForResult(intent, requestCode)
    }

    fun handleResult(context:Context, resultCode: Int): Bitmap? {
        if (resultCode == Activity.RESULT_OK &&
            ::photoFile.isInitialized &&
            photoFile.exists()
        ) {
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val bwBitmap = blackAndWhite(bitmap)

            val outSource = File(context.getExternalFilesDir(null), "photo_cap.jpg")
            FileOutputStream(outSource).use {
                bwBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            return bwBitmap
        }
        return null
    }



    private fun blackAndWhite(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height
        val bwBitmap = createBitmap(width, height)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = original[x, y]
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val gray = (0.3 * r + 0.59 * g + 0.11 * b).toInt()
                bwBitmap[x, y] = Color.rgb(gray, gray, gray)
            }
        }
        return bwBitmap
    }
}
