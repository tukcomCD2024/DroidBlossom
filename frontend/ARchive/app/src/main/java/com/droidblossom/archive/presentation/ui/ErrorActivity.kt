package com.droidblossom.archive.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(lastActivityIntent)
        finish()
    }

    private lateinit var binding: ActivityErrorBinding

    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_error)

        binding.tvErrorLog.text = errorText

        binding.btnReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }
    }


    companion object {
        const val EXTRA_INTENT = "EXTRA_INTENT"
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }
}