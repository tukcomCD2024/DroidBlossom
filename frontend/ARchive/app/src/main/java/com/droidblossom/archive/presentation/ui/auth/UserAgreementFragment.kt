package com.droidblossom.archive.presentation.ui.auth

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentUserAgreementBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAgreementFragment :
    BaseFragment<AuthViewModelImpl, FragmentUserAgreementBinding>(R.layout.fragment_user_agreement) {

    lateinit var navController: NavController

    override val viewModel: AuthViewModelImpl by activityViewModels()

    override fun observeData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val layoutParams = binding.viewHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.viewHeaderTitle.layoutParams = layoutParams
        initView()
    }

    private fun initView() {
        with(binding) {
            privacyAgreeTV.apply {
                text = HtmlCompat.fromHtml(getString(R.string.privacyAgreeText), HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
            }
            serviceAgreementTV.apply {
                text = HtmlCompat.fromHtml(getString(R.string.serviceAgreeText), HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
            }
            locationAgreeTV.apply {
                text = HtmlCompat.fromHtml(getString(R.string.locationAgreeText), HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}