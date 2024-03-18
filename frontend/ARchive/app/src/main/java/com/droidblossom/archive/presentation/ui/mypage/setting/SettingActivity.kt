package com.droidblossom.archive.presentation.ui.mypage.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivitySettingBinding
import com.droidblossom.archive.databinding.FragmentMyPageBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity :  BaseActivity<SettingViewModelImpl, ActivitySettingBinding>(R.layout.activity_setting) {
    override val viewModel: SettingViewModelImpl by viewModels()
    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView(){

    }
}