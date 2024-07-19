package com.droidblossom.archive.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.databinding.ActivityNetworkConnectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetworkConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkConnectionBinding
    private var isCheckingNetwork = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNetworkConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.networkSettingBtn.setOnClickListener {
            openNetworkSettings()
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        startNetworkCheck()
    }

    private fun startNetworkCheck() {
        isCheckingNetwork = true
        lifecycleScope.launch {
            checkNetworkStatus()
        }
    }

    private fun openNetworkSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }

    private suspend fun checkNetworkStatus() {
        while (isCheckingNetwork) {
            if (ARchiveApplication.isOnline()) {
                finishActivityWithNetwork()
                break
            }
            delay(1000)
        }
    }

    private fun finishActivityWithNetwork() {
        isCheckingNetwork = false
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NetworkConnectionActivity::class.java)
    }
}