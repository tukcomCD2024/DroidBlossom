package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingUserModifyBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingUserModifyFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingUserModifyBinding>(R.layout.fragment_setting_user_modify) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {
        binding.backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun observeData() {

    }
}