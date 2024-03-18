package com.droidblossom.archive.presentation.ui.mypage.setting

import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingMainBinding
import com.droidblossom.archive.presentation.base.BaseFragment

class SettingMainFragment :  BaseFragment<SettingViewModelImpl, FragmentSettingMainBinding>(R.layout.fragment_setting_main) {
    override val viewModel: SettingViewModelImpl by viewModels()

    override fun observeData() {}
}