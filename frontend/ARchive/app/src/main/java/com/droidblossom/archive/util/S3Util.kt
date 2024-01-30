package com.droidblossom.archive.util

import android.content.Context
import android.util.Log
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.droidblossom.archive.BuildConfig
import java.io.File
import javax.inject.Inject

class S3Util @Inject constructor(private val context: Context) {

    private val accessKey = BuildConfig.S3ACCESSKEY;
    private val secretKey = BuildConfig.S3SECRETKEY
    private var region: Region = Region.getRegion(Regions.AP_NORTHEAST_2)

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
}

