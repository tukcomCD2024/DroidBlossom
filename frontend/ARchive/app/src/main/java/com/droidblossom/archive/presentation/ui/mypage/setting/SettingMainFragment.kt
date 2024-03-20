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
import com.droidblossom.archive.presentation.customview.CommonDialogFragment
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.util.DataStoreUtils
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingMainFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingMainBinding>(R.layout.fragment_setting_main) {

    override val viewModel: SettingViewModelImpl by viewModels()

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

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
                            val sheet = CommonDialogFragment.newIntent("정말 로그아웃 하시겠습니까?","로그아웃") {
                                CoroutineScope(Dispatchers.IO).launch {
                                    dataStoreUtils.resetTokenData()
                                }
                                AuthActivity.goAuth(requireContext())
                            }
                            sheet.show(parentFragmentManager, "logoutDialog")
                        }

                        SettingViewModel.SettingMainEvent.GoNotice -> {

                        }

                        SettingViewModel.SettingMainEvent.GoNotification -> {

                        }

                        SettingViewModel.SettingMainEvent.GoUser -> {

                        }

                        is SettingViewModel.SettingMainEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}