package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingUserBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingUserFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingUserBinding>(R.layout.fragment_setting_user) {

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

        binding.modifyBtn.setOnClickListener{
            navController.navigate(R.id.action_settingUserFragment_to_settingUserModifyFragment)
        }
    }

    override fun observeData() {

    }
}