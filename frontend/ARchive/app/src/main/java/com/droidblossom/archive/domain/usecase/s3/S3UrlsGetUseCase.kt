package com.droidblossom.archive.domain.usecase.s3

import android.util.Log
import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto
import com.droidblossom.archive.domain.model.s3.S3Urls
import com.droidblossom.archive.domain.repository.S3Repository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class S3UrlsGetUseCase @Inject constructor(
    private val repository: S3Repository
) {
    suspend operator fun invoke(request: S3UrlRequestDto) =
        flow<RetrofitResult<S3Urls>> {
            try {
                emit(repository.getS3Url(request)
                    .onSuccess {

                    }.onFail {

                    }.onException {
                        throw Exception(it)
                    })
            } catch (e: Exception) {
                Log.d("예외확인", "$e")
                e.printStackTrace()
            }
        }
}