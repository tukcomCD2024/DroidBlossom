package com.droidblossom.archive

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.droidblossom.archive.util.ExceptionHandler
import com.droidblossom.archive.util.NetworkStatusChecker
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@HiltAndroidApp
class ARchiveApplication : Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
        context = applicationContext
        networkConnectionChecker = NetworkStatusChecker(context)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        setCrashHandler()
        dummyCoroutines()

        // 키 값 알아내기
//        AppSignatureHelper(this@ARchiveApplication).apply {
//            Log.d("key1", "hash : ${appSignature}")
//        }
//        getHashKey()
        // Kakao Sdk 초기화
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun dummyCoroutines(){
        GlobalScope.launch(Dispatchers.Default) {

            GlobalScope.launch(Dispatchers.IO) {}

            GlobalScope.launch(Dispatchers.Main) {}

            GlobalScope.launch(Dispatchers.Unconfined) {}

        }
    }

    private fun setCrashHandler() {
        val crashlyticsExceptionHandler = Thread.getDefaultUncaughtExceptionHandler() ?: return
        Thread.setDefaultUncaughtExceptionHandler(
            ExceptionHandler(
                this,
                crashlyticsExceptionHandler
            )
        )
    }

    override fun onStop(owner: LifecycleOwner) {
        networkConnectionChecker.unregister()
        super.onStop(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        networkConnectionChecker.register()
        super.onStart(owner)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getString(@StringRes stringResId: Int): String {
            return context.getString(stringResId)
        }

        private lateinit var networkConnectionChecker: NetworkStatusChecker
        fun isOnline() = networkConnectionChecker.isOnline()

        fun getContext() : Context = context

    }

//    private fun getHashKey() {
//        //
//        var packageInfo: PackageInfo? = null
//        try {
//            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
//        for (signature in packageInfo!!.signatures) {
//            try {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("key2", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            } catch (e후: NoSuchAlgorithmException) {
//                Log.e("key3", "Unable to get MessageDigest. signature=$signature", e)
//            }
//        }
//    }
}