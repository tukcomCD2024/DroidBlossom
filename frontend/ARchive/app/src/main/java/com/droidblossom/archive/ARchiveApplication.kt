package com.droidblossom.archive

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.droidblossom.archive.util.AppSignatureHelper
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@HiltAndroidApp
class ARchiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 키 값 알아내기
//        AppSignatureHelper(this@ARchiveApplication).apply {
//            Log.d("key1", "hash : ${appSignature}")
//        }
//        getHashKey()
        // Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
    private fun getHashKey() {
        //
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("key2", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("key3", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }
}