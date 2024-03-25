package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingAgreeBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl

class SettingAgreeFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingAgreeBinding>(R.layout.fragment_setting_agree) {

    override val viewModel: SettingViewModelImpl by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }

    override fun observeData() {

    }
}