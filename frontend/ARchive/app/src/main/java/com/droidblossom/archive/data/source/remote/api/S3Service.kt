package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.s3.request.S3OneUrlRequestDto
import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto
import com.droidblossom.archive.data.dto.s3.response.S3UrlResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface S3Service {

    @POST("s3/upload-url")
    suspend fun getUploadUrlsApi(
        @Body request : S3UrlRequestDto
    ) : Response<ResponseBody<S3UrlResponseDto>>

    @POST("s3/image/upload-url")
    suspend fun getUpLoadUrlApi(
        @Body request: S3OneUrlRequestDto
    ) : Response<ResponseBody<String>>
}