package com.droidblossom.archive.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    fun convertUriToJpegFile(context: Context, uri: Uri, targetFilename: String): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.cacheDir, "$targetFilename.jpeg")

        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                val buffer = ByteArray(4 * 1024) // 4KB buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        return if (outputFile.exists()) outputFile else null
    }

    fun convertUriToVideoFile(context: Context, uri: Uri, targetFilename: String): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.cacheDir, "$targetFilename.mp4")

        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                val buffer = ByteArray(8 * 1024) // 8KB buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        return if (outputFile.exists()) outputFile else null
    }
}