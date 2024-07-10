package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingUserBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.CommonDialogFragment
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModel
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingUserFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingUserBinding>(R.layout.fragment_setting_user) {

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
            if (viewModel.isOnlyProfile.value) {
                requireActivity().finish()
            } else {
                navController.popBackStack()
            }
        }

        binding.phoneCV.setOnClickListener {
            val sheet = CommonDialogFragment.newIntent("번호를 바꾸겠습니까?", "네") {
                startActivity(AuthActivity.newIntent(requireContext(), true))
            }
            sheet.show(parentFragmentManager, "logoutDialog")
        }

        binding.modifyBtn.setOnClickListener {
            navController.navigate(R.id.action_settingUserFragment_to_settingUserModifyFragment)
        }

        binding.tagSwitch.setOnCheckedChangeListener { _, b ->
            viewModel.changeTagSearch(b)
        }

        binding.phoneSwitch.setOnCheckedChangeListener { _, b ->
            viewModel.changePhoneSearchV(b)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.isOnlyProfile.value) {
                        requireActivity().finish()
                    } else {
                        navController.popBackStack()
                    }
                }
            })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingUserEvents.collect {  event ->
                    when(event) {
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