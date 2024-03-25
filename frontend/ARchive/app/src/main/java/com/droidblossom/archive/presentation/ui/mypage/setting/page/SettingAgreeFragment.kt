package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingAgreeBinding
import com.droidblossom.archive.domain.model.setting.Agree
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.setting.adapter.AgreeRVA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingAgreeFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingAgreeBinding>(R.layout.fragment_setting_agree) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController

    private val agreeAdapter by lazy {
        AgreeRVA()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        initRVA()
    }

    private fun initRVA() {
        binding.adapter.adapter = agreeAdapter
        agreeAdapter.submitList(
            listOf(
                Agree("이용약관 1장", getString(R.string.agree_content)),
                Agree("이용약관 2장", getString(R.string.agree_content)),
                Agree("사용자 정보 정책", getString(R.string.agree_content)),
            )
        )
        binding.backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun observeData() {

    }
}