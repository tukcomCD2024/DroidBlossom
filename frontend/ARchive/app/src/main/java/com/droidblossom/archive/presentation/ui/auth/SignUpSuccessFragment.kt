package com.droidblossom.archive.presentation.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignUpSuccessBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpSuccessFragment : BaseFragment<AuthViewModel, FragmentSignUpSuccessBinding>(R.layout.fragment_sign_up_success) {

    override val viewModel : AuthViewModel by activityViewModels()

    override fun observeData() {}

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        MainActivity.goMain(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler.postDelayed(runnable, 2000)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}