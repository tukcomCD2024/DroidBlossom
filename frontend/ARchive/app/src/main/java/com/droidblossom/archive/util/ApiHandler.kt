package com.droidblossom.archive.util

import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.di.RetrofitModule
import retrofit2.Response

suspend fun <T : Any, R : Any> apiHandler(
    execute: suspend () -> Response<T>,
    mapper: (T) -> R
): RetrofitResult<R> {
    if (ARchiveApplication.isOnline().not()) {
        return RetrofitResult.Error(Exception(RetrofitModule.NETWORK_EXCEPTION_OFFLINE))
    }

    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful) {
            body?.let {
                RetrofitResult.Success(mapper(it))
            } ?: run {
                throw NullPointerException(RetrofitModule.NETWORK_EXCEPTION_RESULT_IS_NULL)
            }
        } else {
            getFailRetrofitResult(body, response)
        }
    } catch (e: Exception) {
        RetrofitResult.Error(e)
    }
}


private fun <T : Any> getFailRetrofitResult(body: T?, response: Response<T>) = body?.let {
    RetrofitResult.Fail(statusCode = response.code(), message = it.toString())
} ?: run {
    RetrofitResult.Fail(statusCode = response.code(), message = response.message())
}