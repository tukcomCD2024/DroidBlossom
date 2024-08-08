package com.droidblossom.archive.util

import android.util.Log
import com.droidblossom.archive.di.RetrofitModule
import com.droidblossom.archive.presentation.model.AppEvent
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import java.io.IOException

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response

        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            throw IOException(RetrofitModule.NETWORK_EXCEPTION_OFFLINE)
        }

        if (!response.isSuccessful) {
            when (response.code) {
                404 -> {
                    // 404 에러 처리 로직
                    throw IOException("Resource not found")
                }
                500 -> {
                    // 500 에러 처리 로직
                    throw IOException("Server error")
                }
                502 -> {
                    EventBus.getDefault().post(AppEvent.BadGateEvent)
                }
                else -> {
                    throw IOException(RetrofitModule.NETWORK_EXCEPTION_RESULT_IS_NULL)
                }
            }
        }

        return response
    }
}