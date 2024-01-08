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

        }
    }

    override fun observeData() {}

}