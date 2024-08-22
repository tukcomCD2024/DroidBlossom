package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.BuildConfig
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
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
            webView.loadUrl(BuildConfig.TERMS_OF_USE_URL)
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun observeData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.main_bg_1)
    }
}