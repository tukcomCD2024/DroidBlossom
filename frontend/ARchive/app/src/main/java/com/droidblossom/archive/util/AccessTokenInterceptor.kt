package com.droidblossom.archive.util

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val ds : DataStoreUtils
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwt: String = runBlocking { ds.fetchAccessToken() }
        Log.d("토큰",jwt)
        jwt.let {
            builder.addHeader("Authorization", "Bearer $jwt")
        } ?: run {

        }

        return chain.proceed(builder.build())
    }
}