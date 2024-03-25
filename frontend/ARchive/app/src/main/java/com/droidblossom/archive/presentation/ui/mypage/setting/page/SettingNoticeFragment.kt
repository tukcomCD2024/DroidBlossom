package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingNoticeBinding
import com.droidblossom.archive.databinding.FragmentSettingNotificationBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModel
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingNoticeFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingNoticeBinding>(R.layout.fragment_setting_notice) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {}

    override fun observeData() {}
}