package com.febiarifin.storyappsubmissiondicoding.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.febiarifin.storyappsubmissiondicoding.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

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

fun String?.getTimeAgoFormat(): String {
    if (this.isNullOrEmpty()) return "Unknown"

    val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val sdf = SimpleDateFormat(format, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }

    val pastTime = sdf.parse(this)?.time ?: return "Unknown"
    val diff = System.currentTimeMillis() - pastTime

    val oneMin = 60_000L
    val oneHour = 60 * oneMin
    val oneDay = 24 * oneHour
    val oneMonth = 30 * oneDay
    val oneYear = 365 * oneDay

    return when {
        diff >= oneYear -> "${diff / oneYear} years ago"
        diff >= oneMonth -> "${diff / oneMonth} months ago"
        diff >= oneDay -> "${diff / oneDay} days ago"
        diff >= oneHour -> "${diff / oneHour} hours ago"
        diff >= oneMin -> "${diff / oneMin} min ago"
        else -> "Just now"
    }
}