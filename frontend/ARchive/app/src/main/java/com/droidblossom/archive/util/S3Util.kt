package com.droidblossom.archive.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class S3Util @Inject constructor(private val context: Context) {

    /*
    private val accessKey = BuildConfig.S3ACCESSKEY;
    private val secretKey = BuildConfig.S3SECRETKEY
    private var region: Region = Region.getRegion(Regions.AP_NORTHEAST_2)
    private var bucketName = BuildConfig.S3BUCKET
    fun uploadFile(fileName: String, file: File) {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()
        TransferNetworkLossHandler.getInstance(context)

        val uploadObserver = transferUtility.upload(BuildConfig.S3BUCKET, fileName, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    // 업로드가 완료되었을 때의 처리
                    Log.d("MYTAG", "업러드 끝")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = ((current.toDouble() / total) * 100.0).toInt()
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.toString()} ")
            }
        })
    }
    */
    suspend fun uploadImageWithPresignedUrl(file: File, signedUrl: String) = withContext(Dispatchers.IO) {
        val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(signedUrl)
            .put(requestBody)
            .build()

        try {
            val response = OkHttpClient().newCall(request).execute()

            if (response.isSuccessful) {
                Log.d("S3Upload", "File(Image) uploaded successfully: ${file.name}")
            } else {
                val errorBody = response.body?.string() ?: "No error details"
                Log.e("S3Upload", "Failed to upload file(Image): ${file.name}, Response: $errorBody")
                throw IOException("Failed to upload file(Image): ${file.name}, Response: $errorBody")
            }
        }catch (e:Exception){
            Log.e("S3Upload", "File upload failed(Image): ${file.name}", e)

        }

    }

    suspend fun uploadVideoWithPresignedUrl(file: File, signedUrl: String) = withContext(Dispatchers.IO) {
        val requestBody = file.asRequestBody("video/mp4".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(signedUrl)
            .put(requestBody)
            .build()

        try {
            val response = OkHttpClient().newCall(request).execute()

            if (response.isSuccessful) {
                Log.d("S3Upload", "File(Video) uploaded successfully: ${file.name}")
            } else {
                val errorBody = response.body?.string() ?: "No error details"
                Log.e("S3Upload", "Failed to upload file(Video): ${file.name}, Response: $errorBody")
                throw IOException("Failed to upload file(Video): ${file.name}, Response: $errorBody")
            }
        }catch (e:Exception){
            Log.e("S3Upload", "File upload failed(Video): ${file.name}", e)

        }

    }
}

