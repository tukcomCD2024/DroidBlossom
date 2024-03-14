package com.droidblossom.archive.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.droidblossom.archive.R
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.MyFirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var dataStoreUtils: DataStoreUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // dataStoreUtils 사용
        lifecycleScope.launch {
            delay(1000)
            if (dataStoreUtils.fetchAccessToken().isNotEmpty() && dataStoreUtils.fetchRefreshToken().isNotEmpty()) {
                MainActivity.goMain(this@SplashActivity)
            } else {
                AuthActivity.goAuth(this@SplashActivity)
                finish()
            }
        }

//        handleIntent(intent)
    }
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        intent?.let {
//            setIntent(it)
//            handleIntent(it)
//        }
//    }
//    private fun handleIntent(intent: Intent) {
//        val destination = intent.getStringExtra("fragmentDestination")
//        Log.d("어디로","스플 - 어디로 가야하오 데스티네이션: $destination")
//
//        intent.extras?.let { bundle ->
//            val nick = bundle.getString("click_action")
//            Log.d("어디로","스플 - 어디로 가야하오 엑스트라: $nick")
//        }
//
//    }

}