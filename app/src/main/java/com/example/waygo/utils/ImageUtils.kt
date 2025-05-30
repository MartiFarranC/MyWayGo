package com.example.waygo.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG_IMAGE_UTIL = "ImageUtils"

/** Location: /data/data/<package>/files/images/ */
private fun imageDir(context: Context): File =
    File(context.filesDir, "images").apply {
        if (!exists()) {
            val created = mkdirs()
            Log.d(TAG_IMAGE_UTIL, "Image directory created: $absolutePath, Success: $created")
        } else {
            Log.d(TAG_IMAGE_UTIL, "Image directory already exists: $absolutePath")
        }
    }

fun saveBitmapInternal(context: Context, bitmap: Bitmap): Uri {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "IMG_${timestamp}.jpg"
    val targetDir = imageDir(context)
    val file = File(targetDir, fileName)

    Log.d(TAG_IMAGE_UTIL, "Attempting to save bitmap to: ${file.absolutePath}")

    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        val resultUri = Uri.fromFile(file)
        Log.d(TAG_IMAGE_UTIL, "Bitmap saved successfully. URI: $resultUri, Exists: ${file.exists()}, Size: ${file.length()} bytes")
        return resultUri
    } catch (e: IOException) {
        Log.e(TAG_IMAGE_UTIL, "Error saving bitmap internally: ${e.message}", e)
        return Uri.EMPTY // Return an empty Uri on failure
    }
}

fun copyUriInternal(context: Context, originalUri: Uri): Uri {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "IMG_GALLERY_${timestamp}.jpg"
    val targetDir = imageDir(context)
    val destFile = File(targetDir, fileName)

    Log.d(TAG_IMAGE_UTIL, "Attempting to copy URI from: $originalUri to: ${destFile.absolutePath}")

    try {
        context.contentResolver.openInputStream(originalUri)?.use { inputStream ->
            FileOutputStream(destFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        val resultUri = Uri.fromFile(destFile)
        Log.d(TAG_IMAGE_UTIL, "URI copied successfully. URI: $resultUri, Exists: ${destFile.exists()}, Size: ${destFile.length()} bytes")
        return resultUri
    } catch (e: IOException) {
        Log.e(TAG_IMAGE_UTIL, "Error copying URI internally: ${e.message}", e)
        return Uri.EMPTY
    }
}