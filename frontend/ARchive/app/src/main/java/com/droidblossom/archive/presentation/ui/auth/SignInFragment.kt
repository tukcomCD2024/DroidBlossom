package com.droidblossom.archive.presentation.ui.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.BuildConfig
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignInBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.di.SocialLoginUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
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
            override fun onLoginSuccess(social: AuthViewModel.Social) {
                viewModel.SignInSuccess(social)
            }

            override fun onLoginFailure(error: Throwable) {
                // 에러 처리 로직
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
                viewModel.signInEvent.collect() { event ->
                    if (event == AuthViewModel.SignInResult.SUCCESS){
                        // 서버와 통신 할 때 변경할 것

                        if (viewModel.signInSocial.value == AuthViewModel.Social.KAKAO){
                            // 카카오일 때 회원가입 로직
                            MainActivity.goMain(requireContext())
                        }
                        if (viewModel.signInSocial.value == AuthViewModel.Social.GOOGLE){
                            // 구글일 때 메인
                            viewModel.signInToSignUp()
                        }
                    }
                }
            }
        }
    }

}