package com.droidblossom.archive.presentation.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCertificationBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.util.AuthOtpReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertificationFragment : AuthOtpReceiver.OtpReceiveListener,BaseFragment<AuthViewModelImpl, FragmentCertificationBinding>(R.layout.fragment_certification) {


    lateinit var navController: NavController
    override val viewModel : AuthViewModelImpl by activityViewModels()
    private var smsReceiver : AuthOtpReceiver? = null

    override fun onResume() {
        super.onResume()
        viewModel.initTimer()
        if (binding.certificationNumberEditText1.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.certificationNumberEditText1, InputMethodManager.SHOW_IMPLICIT)
        }
        viewModel.submitPhoneNumber()
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
                viewModel.certificationEvent(AuthViewModel.CertificationEvent.ReSend)
                viewModel.reSend()
            }

            setupAutoFocusOnLength(null, certificationNumberEditText1, certificationNumberEditText2)
            setupAutoFocusOnLength(certificationNumberEditText1, certificationNumberEditText2, certificationNumberEditText3)
            setupAutoFocusOnLength(certificationNumberEditText2, certificationNumberEditText3, certificationNumberEditText4)
            setupAutoFocusOnLength(certificationNumberEditText3, certificationNumberEditText4, null)
        }
    }
    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.certificationNumber
                    .filter { it.length == 4 }
                    .collect { certificationNum ->
                        // 길이가 4일 때의 처리 로직
                        viewModel.submitCertificationNumber()
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.certificationEvents.collect { event ->
                    when (event) {

                        is AuthViewModel.CertificationEvent.ReSend -> {
                            // 서버에게 재전송 요청
                            viewModel.initTimer()
                            viewModel.submitPhoneNumber()
                        }

                        is AuthViewModel.CertificationEvent.SubmitCertificationCode -> {
                            // api 통신
                            viewModel.certificationEvent(AuthViewModel.CertificationEvent.NavigateToSignUpSuccess)
                        }

                        is AuthViewModel.CertificationEvent.NavigateToSignUpSuccess -> {
                            if(navController.currentDestination?.id != R.id.signUpSuccessFragment) {
                                navController.navigate(R.id.action_certificationFragment_to_signUpSuccessFragment)
                            }
                        }

                        is AuthViewModel.CertificationEvent.failCertificationCode -> {
                            binding.certificationNumberEditText1.requestFocus()
                        }

                    }

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

    override fun onOtpReceived(otp: String) {
        Toast.makeText(requireContext(), otp, Toast.LENGTH_SHORT).show()
    }
    private fun startSmsRetriever() {
        SmsRetriever.getClient(requireContext()).startSmsRetriever().also { task ->
            task.addOnSuccessListener {
                if (smsReceiver == null) {
                    smsReceiver = AuthOtpReceiver().apply {
                        setOtpListener(this@CertificationFragment)
                    }
                }
                requireContext().registerReceiver(smsReceiver, smsReceiver!!.doFilter())
            }

            task.addOnFailureListener {
                stopSmsRetriever()
            }
        }
    }

    private fun stopSmsRetriever() {
        if (smsReceiver != null) {
            requireContext().unregisterReceiver(smsReceiver)
            smsReceiver = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}