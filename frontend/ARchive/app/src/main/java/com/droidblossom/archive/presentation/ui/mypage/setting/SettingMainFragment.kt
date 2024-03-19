package com.droidblossom.archive.presentation.ui.mypage.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingMainBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.coroutines.launch

class SettingMainFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingMainBinding>(R.layout.fragment_setting_main) {
    override val viewModel: SettingViewModelImpl by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.settingMainEvents.collect{ event->
                    when(event){
                        SettingViewModel.SettingMainEvent.Back -> {
                            (activity as SettingActivity).finish()
                        }

                        SettingViewModel.SettingMainEvent.GoAgree -> {

                        }

                        SettingViewModel.SettingMainEvent.GoInquire -> {

                        }

                        SettingViewModel.SettingMainEvent.GoLicenses -> {
                            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                        }

                        SettingViewModel.SettingMainEvent.GoLogout -> {

                        }

                        SettingViewModel.SettingMainEvent.GoNotice -> {

                        }

                        SettingViewModel.SettingMainEvent.GoNotification -> {

                        }

                        SettingViewModel.SettingMainEvent.GoUser -> {

                        }

                        is SettingViewModel.SettingMainEvent.ShowToastMessage -> {

                        }

                        else -> {}
                    }
                }
            }
        }
    }
}