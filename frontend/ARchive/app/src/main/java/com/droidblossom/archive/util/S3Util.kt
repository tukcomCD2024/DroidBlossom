package com.droidblossom.archive.util

import android.content.Context
import android.util.Log
import com.amazonaws.HttpMethod
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.droidblossom.archive.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import javax.inject.Inject

class S3Util @Inject constructor(private val context: Context) {

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
    suspend fun putObjectWithPresignedUrl(fileName: String, file: File) = withContext(Dispatchers.IO) {
        // AWS Credentials 설정
        val awsCredentials: AWSCredentials = BasicAWSCredentials(BuildConfig.S3ACCESSKEY, BuildConfig.S3SECRETKEY)
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val presignedPutRequest = GeneratePresignedUrlRequest(BuildConfig.S3BUCKET, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(Date(System.currentTimeMillis() + 3600000)) // 1시간 후 만료

        val presignedUrl = s3Client.generatePresignedUrl(presignedPutRequest)

        val request = Request.Builder()
            .url(presignedUrl.toURI().toURL())
            .put(RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file))
            .build()

        try {
            val response = OkHttpClient().newCall(request).execute()
            if (response.isSuccessful) {
                Log.d("S3Upload", "File uploaded successfully: $fileName")
            } else {
                throw IOException("Failed to upload file: ${response}")
            }
        } catch (e: Exception) {
            Log.e("S3Upload", "File upload failed: $fileName", e)
        }
    }
}

