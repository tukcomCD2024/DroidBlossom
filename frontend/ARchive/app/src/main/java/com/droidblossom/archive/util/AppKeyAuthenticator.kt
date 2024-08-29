package com.droidblossom.archive.util

import com.droidblossom.archive.BuildConfig.DEFAULT_APP_KEY
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AppKeyAuthenticator : Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("Default-Key", DEFAULT_APP_KEY)
        return chain.proceed(builder.build())
    }

}