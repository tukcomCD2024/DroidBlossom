package com.droidblossom.archive.presentation.ui.mypage.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivitySettingBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity :
    BaseActivity<SettingViewModelImpl, ActivitySettingBinding>(R.layout.activity_setting) {
    override val viewModel: SettingViewModelImpl by viewModels()
    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        val layoutParams =
            binding.settingFragmentContainer.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.settingFragmentContainer.layoutParams = layoutParams
    }

    companion object {
        const val SETTING = "setting"
        const val ONLY_PROFILE = "only_profile"

        fun newIntent(context: Context, profile: Boolean) =
            Intent(context, SettingActivity::class.java).apply {
                putExtra(ONLY_PROFILE, profile)
            }
    }
}