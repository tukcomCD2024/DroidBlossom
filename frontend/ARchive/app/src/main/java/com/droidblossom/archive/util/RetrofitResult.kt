package com.droidblossom.archive.util

sealed class RetrofitResult<out T> {
    data class Success<T>(val data: T) : RetrofitResult<T>()
    data class Fail(val statusCode: Int, val message: String) : RetrofitResult<Nothing>()
    data class Error(val exception: Exception) : RetrofitResult<Nothing>()
}

inline fun <T> RetrofitResult<T>.onSuccess(action: (T) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Success) {
        action(data)
    }
    return this
}

inline fun <T> RetrofitResult<T>.onFail(resultCode: (Int) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Fail) {
        resultCode(this.statusCode)
    }
    return this
}

inline fun <T> RetrofitResult<T>.onError(action: (Exception) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Fail) {
        action(IllegalArgumentException("code : ${this.statusCode}, message : ${this.message}"))
    } else if (this is RetrofitResult.Error) {
        action(this.exception)
    }
    return this
}

inline fun <T> RetrofitResult<T>.onException(action: (Exception) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Error) {
        action(this.exception)
    }
    return this
}