package com.droidblossom.archive

import android.app.Application
import android.util.Log
import com.droidblossom.archive.util.AppSignatureHelper
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ARchiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 키 값 알아내기
//        AppSignatureHelper(this@ARchiveApplication).apply {
//            Log.d("hash", "hash : ${appSignature}")
//        }
        // Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}