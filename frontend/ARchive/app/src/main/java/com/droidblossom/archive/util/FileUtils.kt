package com.droidblossom.archive.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

//        val fileSize = outputFile.length()
//        Log.d("ConvertUriToJpeg", "File size: $fileSize bytes")

        return if (outputFile.exists()) outputFile else null
    }

    suspend fun resizeBitmapFromUri(context: Context, uri: Uri, targetFilename: String): File? = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputStream, null, options)

            var sampleSize = 1
            while (options.outWidth / sampleSize > 1024 || options.outHeight / sampleSize > 1024) {
                sampleSize *= 2
            }

            val resizeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            val resizedBitmap = context.contentResolver.openInputStream(uri)?.use { resizedInputStream ->
                BitmapFactory.decodeStream(resizedInputStream, null, resizeOptions)
            }

            val outputFile = File(context.cacheDir, "$targetFilename.png")
            resizedBitmap?.let { bitmap ->
                val rotation = getRotation(context, uri)
                val rotatedBitmap = rotateBitmap(bitmap, rotation)

                FileOutputStream(outputFile).use { out ->
                    rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                bitmap.recycle()
                rotatedBitmap.recycle()
            }

            return@withContext outputFile.takeIf { it.exists() }
        }
    }

    private fun getRotation(context: Context, photoUri: Uri): Int {
        val inputStream = context.contentResolver.openInputStream(photoUri) ?: return 0
        val exifInterface = ExifInterface(inputStream)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        inputStream.close()

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        if (degrees == 0) return bitmap

        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    suspend fun convertUriToVideoFile(context: Context, uri: Uri, targetFilename: String): File? = withContext(Dispatchers.IO) {
        val outputFile = File(context.cacheDir, "$targetFilename.mp4")

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(outputFile).use { fileOutputStream ->
                val buffer = ByteArray(8 * 1024) // 8KB buffer size
                while (true) {
                    val byteCount = inputStream.read(buffer)
                    if (byteCount < 0) break
                    fileOutputStream.write(buffer, 0, byteCount)
                }
                fileOutputStream.flush()
            }
        }

        return@withContext if (outputFile.exists()) outputFile else null
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