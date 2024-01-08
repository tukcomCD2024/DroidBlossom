package com.droidblossom.archive.presentation.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignInBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<AuthViewModel,FragmentSignInBinding>(R.layout.fragment_sign_in) {


    lateinit var navController: NavController

    override val viewModel : AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.kakaoLoginBtn.setOnClickListener {
            viewModel.signInToSignUp()
        }

        binding.googleLoginBtn.setOnClickListener {
            viewModel.signInToSignUp()
        }
    }

    override fun observeData() {

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doneEvent.filter { it == AuthViewModel.AuthFlowEvent.SIGNIN_TO_SIGNUP }
                    .collect { event ->
                        if(navController.currentDestination?.id != R.id.signUpFragment) {
                            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
                        }
                    }
            }
        }

    }
}