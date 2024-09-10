package com.droidblossom.archive.util

import android.util.Log
import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.di.RetrofitModule
import com.droidblossom.archive.presentation.model.AppEvent
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
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
            val rawBody = response.body?.string() ?: ""
            val gson = Gson()
            val responseBody = gson.fromJson(rawBody, ResponseBody::class.java)

            if (response.code == 400) {

                if (responseBody.code == "GLOBAL-006") {
                    EventBus.getDefault().post(AppEvent.AppKeyErrorEvent)
                }
            } else if (response.code == 502) {
                EventBus.getDefault().post(AppEvent.BadGateEvent)
            } else if (response.code == 503){
                EventBus.getDefault().post(AppEvent.BadGateEvent)
            }

            val newResponseBody = rawBody.toResponseBody(response.body?.contentType())
            return response.newBuilder().body(newResponseBody).build()
        }

        return response
    }
}