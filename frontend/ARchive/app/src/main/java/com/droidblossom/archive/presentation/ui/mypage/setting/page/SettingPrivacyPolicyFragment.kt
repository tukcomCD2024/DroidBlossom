package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.BuildConfig.PRIVACY_POLICY_URL
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingPrivacyPolicyBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingPrivacyPolicyFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingPrivacyPolicyBinding>(R.layout.fragment_setting_privacy_policy) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        with(binding){
            with(webView.settings) {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                cacheMode = WebSettings.LOAD_DEFAULT
                textZoom = 100
            }
            webView.loadUrl(PRIVACY_POLICY_URL)
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun observeData() {

    }

}