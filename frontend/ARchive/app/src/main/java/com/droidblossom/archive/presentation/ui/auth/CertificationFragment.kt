package com.droidblossom.archive.presentation.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCertificationBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertificationFragment : BaseFragment<AuthViewModel, FragmentCertificationBinding>(R.layout.fragment_certification) {


    lateinit var navController: NavController
    override val viewModel : AuthViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        viewModel.initTimer()
        if (binding.certificationNumberEditText1.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.certificationNumberEditText1, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        viewModel.startTimer()
        initView()
    }

    private fun initView(){

        with(binding){
            resendBtn.setOnClickListener {
                // 인증번호 재전송
                viewModel.initTimer()
            }

            setupAutoFocusOnLength(null, certificationNumberEditText1, certificationNumberEditText2)
            setupAutoFocusOnLength(certificationNumberEditText1, certificationNumberEditText2, certificationNumberEditText3)
            setupAutoFocusOnLength(certificationNumberEditText2, certificationNumberEditText3, certificationNumberEditText4)
            setupAutoFocusOnLength(certificationNumberEditText3, certificationNumberEditText4, null)
        }
    }

    override fun observeData() {

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doneEvent.filter { it == AuthViewModel.AuthFlowEvent.CERTIFICATION_TO_SIGNUPSUCCESS }
                    .collect { event ->
                        if(navController.currentDestination?.id != R.id.signUpSuccessFragment) {
                            navController.navigate(R.id.action_certificationFragment_to_signUpSuccessFragment)
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.certificationNumber
                    .filter { it.length == 4 }
                    .collect { certificationNum ->
                        // 길이가 4일 때의 처리 로직
                        viewModel.certificationToSignUpSuccess()
                    }
            }
        }

    }

    private fun setupAutoFocusOnLength(previousEditText: EditText?, currentEditText: EditText, nextEditText: EditText?) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length == 1) {
                        nextEditText?.requestFocus()
                    } else if (s.isEmpty()) {
                        previousEditText?.requestFocus()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}