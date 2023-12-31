package com.capstone.roadcrackapp.model.adapter

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.capstone.roadcrackapp.R
import java.io.*
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String {
    val input = ZonedDateTime.parse(dateString)
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    val output = dateTimeFormatter.format(input)
    return output
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateFile(file: File, isBackCamera: Boolean = false) {
    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    val rotation = if (isBackCamera) 90f else -90f
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

//fun reduceFileImage(file: File): File {
//    val bitmap = BitmapFactory.decodeFile(file.path)
//
//    var compressQuality = 100
//    var streamLength: Int
//
//    do {
//        val bmpStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
//        val bmpPicByteArray = bmpStream.toByteArray()
//        streamLength = bmpPicByteArray.size
//        compressQuality -= 5
//    } while (streamLength > 1000000)
//
//    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
//
//    return file
//}

fun reduceFileImage(file: File): File {
    if (file == null ) {
        throw IllegalArgumentException("File tidak valid.")
    }

    val targetWidth = 224
    val targetHeight = 224

    val bitmap = BitmapFactory.decodeFile(file.path)

    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)

    val outputStream: FileOutputStream? = try {
        FileOutputStream(file)
    } catch (e: IOException) {
        e.printStackTrace()
        throw e
    }

    try {
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream?.flush()
    } catch (e: IOException) {
        e.printStackTrace()
        throw e
    } finally {
        outputStream?.close()
    }

    return file
}