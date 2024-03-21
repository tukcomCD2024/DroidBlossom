package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.droidblossom.archive.util.DataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

@AndroidEntryPoint
class SettingNotificationFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingNotificationBinding>(R.layout.fragment_setting_notification) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController

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