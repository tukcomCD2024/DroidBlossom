package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.s3.request.S3OneUrlRequestDto
import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto
import com.droidblossom.archive.data.dto.s3.response.S3UrlResponseDto
import com.droidblossom.archive.data.source.remote.api.S3Service
import com.droidblossom.archive.domain.model.s3.S3Urls
import com.droidblossom.archive.domain.repository.S3Repository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class S3RepositoryImpl @Inject constructor(
    private val s3Service: S3Service
) : S3Repository {

    override suspend fun getS3Url(request: S3UrlRequestDto): RetrofitResult<S3Urls> {
        return apiHandler({ s3Service.getUploadUrlsApi(request) }) { response: ResponseBody<S3UrlResponseDto> -> response.result.toModel() }
    }

    override suspend fun getS3OneUrl(request: S3OneUrlRequestDto): RetrofitResult<String> {
        return apiHandler({ s3Service.getUpLoadUrlApi(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }
}
