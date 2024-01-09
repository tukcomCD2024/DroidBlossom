package com.droidblossom.archive.presentation.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignUpBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<AuthViewModelImpl, FragmentSignUpBinding>(R.layout.fragment_sign_up) {


    private lateinit var navController: NavController
    override val viewModel : AuthViewModelImpl by activityViewModels()

    override fun onResume() {
        super.onResume()
        if (binding.phoneNumberEditText.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.phoneNumberEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        binding.confirmBtn.setOnClickListener {
            // 휴대폰 번호 유효성 검사 필요

//            val rawNumber = viewModel.rawPhoneNumber.value
//            Toast.makeText(requireContext(), rawNumber, Toast.LENGTH_SHORT).show()
            //Toast.makeText(requireContext(), viewModel.phoneNumber.value, Toast.LENGTH_SHORT).show()
            viewModel.signUpToCertification()
        }

    }

    override fun observeData() {
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doneEvent.filter { it == AuthViewModel.AuthFlowEvent.SIGNUP_TO_CERTIFICATION }
                    .collect { event ->
                        if(navController.currentDestination?.id != R.id.certificationFragment) {
                            navController.navigate(R.id.action_signUpFragment_to_certificationFragment)
                        }
                    }
            }
        }
    }

}