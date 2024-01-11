package com.droidblossom.archive.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignInBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<AuthViewModelImpl,FragmentSignInBinding>(R.layout.fragment_sign_in) {


    lateinit var navController: NavController

    override val viewModel : AuthViewModelImpl by activityViewModels()

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("카카오", "로그인 실패 $error")
        } else if (token != null) {
            Log.e("카카오", "로그인 성공 ${token.accessToken}")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.singInEvent.collect() { event ->
                    if (event == AuthViewModel.SignInResult.SUCCESS){
                        // 회원이 아닐 때의 로직이 추가되어야함
                        //viewModel.signInToSignUp()
                        MainActivity.goMain(requireContext())
                    }
                }
            }
        }
    }

    private fun kakaoLogin(){
        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                // 로그인 실패 부분
                if (error != null) {
                    Log.e("카카오", "로그인 실패 $error")
                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback) // 카카오 이메일 로그인
                    }
                    viewModel.SignInSuccess()
                }
                // 로그인 성공 부분
                else if (token != null) {
                    viewModel.SignInSuccess()
                    Log.e("카카오", "로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback) // 카카오 이메일 로그인
        }
    }
}