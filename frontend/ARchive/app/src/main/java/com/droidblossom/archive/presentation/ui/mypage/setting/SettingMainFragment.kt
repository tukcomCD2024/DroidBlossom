package com.droidblossom.archive.presentation.ui.mypage.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingMainBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.CommonDialogFragment
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.util.InquiryEmailSender
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingMainFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingMainBinding>(R.layout.fragment_setting_main) {

    override val viewModel: SettingViewModelImpl by activityViewModels()

    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        if (requireActivity().intent.getBooleanExtra(SettingActivity.ONLY_PROFILE, false)){
            viewModel.onlyProfile()
            navController.navigate(R.id.action_settingMainFragment_to_settingUserFragment)
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingMainEvents.collect { event ->
                    when (event) {
                        SettingViewModel.SettingMainEvent.Back -> {
                            (activity as SettingActivity).finish()
                        }

                        SettingViewModel.SettingMainEvent.GoAgree -> {
                            navController.navigate(R.id.action_settingMainFragment_to_settingAgreeFragment)
                        }

                        SettingViewModel.SettingMainEvent.GoInquire -> {
                            InquiryEmailSender.sendEmail(context = requireContext())
                        }

                        SettingViewModel.SettingMainEvent.GoLicenses -> {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    OssLicensesMenuActivity::class.java
                                )
                            )
                            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                        }

                        SettingViewModel.SettingMainEvent.GoLogout -> {
                            val sheet = CommonDialogFragment.newIntent("로그아웃","정말 로그아웃 하시겠습니까?", "로그아웃") {
                                viewModel.singOutRequest()
                            }
                            sheet.show(parentFragmentManager, "logoutDialog")
                        }

                        SettingViewModel.SettingMainEvent.GoDeleteAccount -> {
                            val sheet = CommonDialogFragment.newIntent(
                                "계정 탈퇴","정말 탈퇴하시겠어요? \n 계정 탈퇴 후에는 24시간 이내에 복구가 가능하고 그 이후에는 모든 데이터가 영구적으로 삭제되며, 되돌릴 수 없습니다.",
                                "계정 탈퇴"
                            ) {
                                viewModel.deleteAccountRequest()
                            }
                            sheet.show(parentFragmentManager, "deleteAccountDialog")
                        }

                        SettingViewModel.SettingMainEvent.GoNotice -> {
                            navController.navigate(R.id.action_settingMainFragment_to_settingNoticeFragment)
                        }

                        SettingViewModel.SettingMainEvent.GoNotification -> {
                            goNotificationIntent()
                            //navController.navigate(R.id.action_settingMainFragment_to_settingNotificationFragment)
                        }

                        SettingViewModel.SettingMainEvent.GoUser -> {
                            navController.navigate(R.id.action_settingMainFragment_to_settingUserFragment)
                        }

                        is SettingViewModel.SettingMainEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        is SettingViewModel.SettingMainEvent.DismissLoading -> {
                            dismissLoading()
                        }

                        is SettingViewModel.SettingMainEvent.ShowLoading -> {
                            showLoading(requireContext())
                        }

                        is SettingViewModel.SettingMainEvent.GoAuthActivity -> {
                            AuthActivity.goAuth(requireContext())
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun goNotificationIntent() {
        val notificationIntent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationIntent.action = android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
            notificationIntent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
        } else {
            notificationIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            notificationIntent.putExtra("app_package", requireContext().packageName)
            notificationIntent.putExtra("app_uid", requireContext().applicationInfo.uid)
        }
        startActivity(notificationIntent)
    }
}