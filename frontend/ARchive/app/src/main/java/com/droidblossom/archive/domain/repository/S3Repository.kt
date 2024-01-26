package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto
import com.droidblossom.archive.domain.model.s3.S3Urls
import com.droidblossom.archive.util.RetrofitResult

interface S3Repository {

    suspend fun getS3Url(request : S3UrlRequestDto) : RetrofitResult<S3Urls>

}