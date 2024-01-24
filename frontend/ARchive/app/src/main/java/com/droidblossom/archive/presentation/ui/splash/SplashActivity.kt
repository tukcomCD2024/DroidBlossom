package com.droidblossom.archive.presentation.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.droidblossom.archive.R
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.util.SharedPreferencesUtils


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPreferencesUtils(this).fetchAccessToken()
                    .isNotEmpty() && SharedPreferencesUtils(this).fetchRefreshToken()
                    .isNotEmpty()
            ) {
                MainActivity.goMain(this@SplashActivity)
            }else{
                AuthActivity.goAuth(this@SplashActivity)
                finish()
            }
        }, 1000)
    }

}