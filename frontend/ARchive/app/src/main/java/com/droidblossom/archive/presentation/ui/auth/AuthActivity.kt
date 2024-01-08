package com.droidblossom.archive.presentation.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityAuthBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<AuthViewModelImpl, ActivityAuthBinding>(R.layout.activity_auth) {

    override val viewModel: AuthViewModelImpl by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun observeData() {

    }

    companion object{
        fun goAuth(context: Context) {
            val intent = Intent(context, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}