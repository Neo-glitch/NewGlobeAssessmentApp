package com.bridge.androidtechnicaltest.core.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

fun getCountryCodeByName(countryName: String): String? {
    val locales = Locale.getAvailableLocales()
    for (locale in locales) {
        if (locale.displayCountry.equals(countryName, ignoreCase = true)) {
            return locale.country
        }
    }
    return null
}

fun createImageFile(context: Context): File {
    val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pupil_images")
    if (!dir.exists()) dir.mkdirs()
    return File(dir, "pupil_${System.currentTimeMillis()}.jpg")
}

fun saveImageToInternalStorage(uri: Uri, context: Context): String {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val dir = File(context.filesDir, "pupil_images")
    if (!dir.exists()) dir.mkdirs()

    val file = File(dir, "pupil_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)

    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()

    return file.absolutePath
}

fun encodeImageToBase64(imagePath: String): String {
    val file = File(imagePath)
    val bytes = file.readBytes()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

fun String.isRemoteImage(): Boolean {
    return this.startsWith("http")
}