package com.droidblossom.archive.presentation.ui.auth

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentUserAgreementBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserAgreementFragment :
    BaseFragment<AuthViewModelImpl, FragmentUserAgreementBinding>(R.layout.fragment_user_agreement) {

    lateinit var navController: NavController

    override val viewModel: AuthViewModelImpl by activityViewModels()

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userAgreementEvents.collect { event ->
                    when(event){
                        is AuthViewModel.UserAgreementEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        is AuthViewModel.UserAgreementEvent.NavigateToSignUp -> {
                            if (navController.currentDestination?.id != R.id.signUpFragment) {
                                navController.navigate(R.id.action_userAgreementFragment_to_signUpFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        val layoutParams = binding.viewHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.viewHeaderTitle.layoutParams = layoutParams
        initView()
    }

    private fun initView() {
        with(binding) {
            privacyAgreementTV.apply {
                text = HtmlCompat.fromHtml(getString(R.string.privacyAgreeText), HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
            }
            serviceAgreementTV.apply {
                text = HtmlCompat.fromHtml(getString(R.string.serviceAgreeText), HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
            }
            locationAgreementTV.apply {
                text = HtmlCompat.fromHtml(getString(R.string.locationAgreeText), HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}