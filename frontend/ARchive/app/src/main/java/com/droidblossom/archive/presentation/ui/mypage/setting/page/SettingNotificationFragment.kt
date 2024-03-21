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
import com.droidblossom.archive.databinding.FragmentSettingNotificationBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModel
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingNotificationFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingNotificationBinding>(R.layout.fragment_setting_notification) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {
        viewModel.getNotificationEnable()

        binding.backBtn.setOnClickListener {
            viewModel.postNotificationEnable(binding.notificationSwitch.isChecked)
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, b ->
            callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                   viewModel.postNotificationEnable(b)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationEnable.collect { enable ->
                    binding.notificationSwitch.isChecked = enable
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingNotificationEvents.collect { event ->
                    when (event) {
                        is SettingViewModel.SettingNotificationEvent.Back -> {
                            navController.popBackStack()
                        }

                        is SettingViewModel.SettingNotificationEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}