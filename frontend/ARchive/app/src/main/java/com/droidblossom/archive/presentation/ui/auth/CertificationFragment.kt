package com.droidblossom.archive.presentation.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CertificationFragment : AuthOtpReceiver.OtpReceiveListener,BaseFragment<AuthViewModelImpl, FragmentCertificationBinding>(R.layout.fragment_certification) {


    lateinit var navController: NavController
    override val viewModel : AuthViewModelImpl by activityViewModels()
    private var smsReceiver : AuthOtpReceiver? = null

    override fun onResume() {
        super.onResume()
        if (binding.certificationNumberEditText1.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.certificationNumberEditText1, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initTimer()

        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        viewModel.startTimer()
        initView()
        addTextWatcher()

        val layoutParams = binding.viewHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.viewHeaderTitle.layoutParams = layoutParams
    }

    private fun initView(){

        with(binding){
            resendBtn.setOnClickListener {
                viewModel.reSend()
                resetCertificationNumber()
            }
        }
    }
    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.certificationNumber.collect { certificationNum ->
                    if (certificationNum.length == 4) {
                        // 길이가 4일 때의 처리 로직
                        viewModel.submitCertificationNumber()
                    } else if (certificationNum.isEmpty()) {
                        binding.certificationNumberEditText1.postDelayed({
                            binding.certificationNumberEditText1.requestFocus()
                        }, 50)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.certificationEvents.collect { event ->
                    when (event) {

                        is AuthViewModel.CertificationEvent.NavigateToSignUpSuccess -> {
                            if(navController.currentDestination?.id != R.id.signUpSuccessFragment) {
                                navController.navigate(R.id.action_certificationFragment_to_signUpSuccessFragment)
                            }
                        }

                        is AuthViewModel.CertificationEvent.VerificationCodeMismatch -> {
                            resetCertificationNumber()
                        }

                        is AuthViewModel.CertificationEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                    }

                }
            }
        }
    }

    private fun addTextWatcher() {
        setupAutoFocusOnLength(null, binding.certificationNumberEditText1, binding.certificationNumberEditText2)
        setupAutoFocusOnLength(binding.certificationNumberEditText1, binding.certificationNumberEditText2, binding.certificationNumberEditText3)
        setupAutoFocusOnLength(binding.certificationNumberEditText2, binding.certificationNumberEditText3, binding.certificationNumberEditText4)
        setupAutoFocusOnLength(binding.certificationNumberEditText3, binding.certificationNumberEditText4, null)
    }


    private fun resetCertificationNumber() {
        viewModel.certificationNumber4.value = ""
        viewModel.certificationNumber3.value = ""
        viewModel.certificationNumber2.value = ""
        viewModel.certificationNumber1.value = ""
    }

    private fun setupAutoFocusOnLength(previousEditText: EditText?, currentEditText: EditText, nextEditText: EditText?): TextWatcher {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s == null) return

                when {
                    s.length == 1 -> nextEditText?.requestFocus()
                    s.isEmpty() -> previousEditText?.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        currentEditText.addTextChangedListener(textWatcher)
        return textWatcher
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