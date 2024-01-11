package com.droidblossom.archive.presentation.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.droidblossom.archive.R
import com.droidblossom.archive.presentation.ui.auth.AuthActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            AuthActivity.goAuth(this@SplashActivity)
            finish()
        }, 1000)
    }

}