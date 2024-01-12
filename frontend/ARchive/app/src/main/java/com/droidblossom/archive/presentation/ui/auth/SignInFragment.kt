package com.droidblossom.archive.presentation.ui.auth

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

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("카카오", "로그인 실패 $error")
        } else if (token != null) {
            Log.e("카카오2", "로그인 성공 ${token.accessToken}")
        }
    }

    private var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == -1) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            googleSignInSuccess(task)
        }
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.kakaoLoginBtn.setOnClickListener {
            //viewModel.signInToSignUp()
            kakaoSignIn()
        }

        binding.googleLoginBtn.setOnClickListener {
            //viewModel.signInToSignUp()
            googleSignIn()
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

    private fun kakaoSignIn(){
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
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
                }
                // 로그인 성공 부분
                else if (token != null) {
                    viewModel.SignInSuccess(AuthViewModel.Social.KAKAO)
                    Log.e("카카오1", "로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback) // 카카오 이메일 로그인
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleLoginLauncher.launch(signInIntent)
    }
    private fun googleSignInSuccess(completedTask: Task<GoogleSignInAccount>) {
        viewModel.SignInSuccess(AuthViewModel.Social.GOOGLE)
        // 회원이 아닐 때의 로직이 추가되어야함

        try {
            val account = completedTask.getResult(ApiException::class.java)
        }
        catch (e: ApiException) {
            Log.w("구글", "signInResult:failed code=" + e.statusCode)
        }
    }

}