package com.droidblossom.archive.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.ar.core.dependencies.e
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar

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

    fun getWebVideoThumbnail(uri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        val thumbnailTime = 1

        try {
            retriever.setDataSource(uri.toString(), HashMap<String, String>())
            return retriever.getFrameAtTime(
                (thumbnailTime * 1000000).toLong(),
                MediaMetadataRetriever.OPTION_CLOSEST
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun resizeUri(context: Context, uri: Uri, resize: Int): Uri? {
        var resizeBitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        try {
            BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri),
                null,
                options
            ); // 1번

            var width = options.outWidth
            var height = options.outHeight
            var sampleSize = 1

            while (true) {//2번
                width /= 2
                height /= 2
                sampleSize *= 2
                if (width / 2 < resize || height / 2 < resize)
                    break
            }

            options.inSampleSize = sampleSize;
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri),
                null,
                options
            ) //3번

            resizeBitmap = bitmap
            Log.d("리사이즈", "$sampleSize")
        } catch (e: Exception) {
            Log.d("리사이즈", "$e")
            e.printStackTrace()
        }
        return getImageUri(context, resizeBitmap)
    }

    private fun getImageUri(inContext: Context?, inImage: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext?.contentResolver,
            inImage,
            "resize" + " - " + Calendar.getInstance().time,
            null
        )
        return Uri.parse(path)
    }
}