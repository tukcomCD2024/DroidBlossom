package com.droidblossom.archive.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.droidblossom.archive.databinding.ActivityErrorBinding
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.InquiryEmailSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ErrorActivity : AppCompatActivity() {
    @Inject lateinit var dataStoreUtils: DataStoreUtils
    override fun onBackPressed() {
        super.onBackPressed()
        navigateBasedOnAuthStatus()
    }

    private lateinit var binding: ActivityErrorBinding

    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.reloadBtn.setOnClickListener {
            navigateBasedOnAuthStatus()
        }

        binding.inquireBtn.setOnClickListener {
            InquiryEmailSender.sendEmail(context = this)
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun navigateBasedOnAuthStatus() {
        lifecycleScope.launch {
            if (dataStoreUtils.fetchAccessToken().isNotEmpty() && dataStoreUtils.fetchRefreshToken().isNotEmpty()) {
                startActivity(Intent(this@ErrorActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@ErrorActivity, AuthActivity::class.java))
            }

            finish()
        }
    }

    companion object {
        const val EXTRA_INTENT = "EXTRA_INTENT"
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }
}