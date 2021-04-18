package com.example.recipes.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class ImageSaver @Inject constructor(private val context: Context) {

    private val saverScope = CoroutineScope(SupervisorJob())

    companion object {
        private const val TAG = "IMAGE_SAVER"
        private const val directoryName = "/Recipe images"
    }


    fun save(bitmap: Bitmap) = saverScope.launch(Dispatchers.IO) {
        val outputStream: OutputStream?
        val fileName = System.currentTimeMillis()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + File.separator + directoryName
                    )
                }

                val imageUri = resolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                outputStream = imageUri?.let { resolver.openOutputStream(it) }
            } else {
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val imageFile = File(directory.absolutePath, "$fileName.jpg")
                outputStream = FileOutputStream(imageFile)
                startMediaScan(Uri.fromFile(imageFile))
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.close()

            showResult("Image saved")
            Log.d(TAG, "Image saved")
        } catch (e: Exception) {
            showResult(e.message.toString())
            Log.e(TAG, e.message.toString())
        }
    }

    private suspend fun showResult(message: String) = withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun startMediaScan(imagePath: Uri) {
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        scanIntent.data = imagePath
        context.sendBroadcast(scanIntent)
    }

}

