package com.droidblossom.archive.presentation.ui.mypage.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivitySettingBinding
import com.droidblossom.archive.databinding.FragmentMyPageBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity.Companion.CREATE_CAPSULE
import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModelImpl
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
        val layoutParams = binding.settingFragmentContainer.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.settingFragmentContainer.layoutParams = layoutParams
    }

    companion object {
        const val SETTING = "setting"

        fun newIntent(context: Context) =
            Intent(context, SettingActivity::class.java)
    }
}