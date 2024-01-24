package com.droidblossom.archive.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val pr : TokenUtils
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val jwt: String? = pr.fetchAccessToken()
        Log.d("accessToken",jwt.toString())

        jwt?.let {
            builder.addHeader("Authorization", "bearer $jwt")
            Log.d("제발",jwt)
        } ?: run {

        }

        return chain.proceed(builder.build())
    }
}