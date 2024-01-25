package com.droidblossom.archive.presentation.ui.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignInBinding
import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.member.CheckStatus
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.util.SocialLoginUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<AuthViewModelImpl,FragmentSignInBinding>(R.layout.fragment_sign_in) {


    lateinit var navController: NavController

    override val viewModel : AuthViewModelImpl by activityViewModels()

    private lateinit var socialLoginUtil: SocialLoginUtil

    private var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            socialLoginUtil.handleGoogleSignInResult(task)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        socialLoginUtil = SocialLoginUtil(requireContext(), object : SocialLoginUtil.LoginCallback {
            override fun onLoginSuccess(memberStatusCheckData : CheckStatus,signUpData : SignUp) {
                viewModel.memberStatusCheck(memberStatusCheckData, signUpData)
            }

            override fun onLoginFailure(error: Throwable) {
                // 에러 처리 로직
                viewModel.signInEvent(AuthViewModel.SignInEvent.SignInFailure(error))
            }
        })

        binding.kakaoLoginBtn.setOnClickListener {
            socialLoginUtil.kakaoSignIn()
        }

        binding.googleLoginBtn.setOnClickListener {
            val signInIntent = socialLoginUtil.googleSignIn()
            googleLoginLauncher.launch(signInIntent)
        }
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInEvents.collect { event ->
                    when (event) {
                        is AuthViewModel.SignInEvent.SocialSignSuccess -> {
                            // 소셜 로그인 성공 - api 통신
                            // 휴대폰 번호까지 인증된 회원 or 처음 또는 인증 안 된 회원
                            if (event.signUpData.socialType == AuthViewModel.Social.GOOGLE){
                                viewModel.signInEvent(AuthViewModel.SignInEvent.NavigateToSignUp)
                            }
                            if (event.signUpData.socialType == AuthViewModel.Social.KAKAO){
                                viewModel.signInEvent(AuthViewModel.SignInEvent.NavigateToMain)
                            }
                        }

                        is AuthViewModel.SignInEvent.NavigateToMain -> {
                            MainActivity.goMain(requireContext())
                        }

                        is AuthViewModel.SignInEvent.SignInFailure -> {
                            // 로그인 실패 처리
                        }

                        is AuthViewModel.SignInEvent.NavigateToSignUp -> {
                            // 회원가입 화면으로 이동
                            if(navController.currentDestination?.id != R.id.signUpFragment) {
                                navController.navigate(R.id.action_signInFragment_to_signUpFragment)
                            }
                        }

                    }
                }
            }
        }
    }

}