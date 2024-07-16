package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingUserModifyBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.CommonDialogFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModel
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingUserModifyFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingUserModifyBinding>(R.layout.fragment_setting_user_modify) {

    override val viewModel: SettingViewModelImpl by activityViewModels()
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        initView()
    }

    private fun initView() {
        binding.backBtn.setOnClickListener {
            val sheet = CommonDialogFragment.newIntent("프로필 수정","프로필 수정을 취소하겠습니까?", "네") {
                navController.popBackStack()
            }
            sheet.show(parentFragmentManager, "logoutDialog")
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val sheet = CommonDialogFragment.newIntent("프로필 수정","프로필 수정을 취소하겠습니까?", "네") {
                        navController.popBackStack()
                    }
                    sheet.show(parentFragmentManager, "logoutDialog")
                }
            })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingUserEvents.collect {  event ->
                    when(event) {
                        is SettingViewModel.SettingUserEvent.Back -> {
                            navController.popBackStack()
                        }

                        is SettingViewModel.SettingUserEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}