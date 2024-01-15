package com.droidblossom.di

import android.content.Context
import android.content.Intent
import android.util.Log
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.user.UserApiClient

class SocialLoginUtil(private val context: Context, private val callback: LoginCallback) {

    interface LoginCallback {
        fun onLoginSuccess(social: AuthViewModel.Social)
        fun onLoginFailure(error: Throwable)
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun kakaoSignIn() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("카카오", "로그인 실패 $error")
                    callback.onLoginFailure(error)
                } else if (token != null) {
                    //Log.e("카카오", "로그인 성공 ${token.accessToken}")
                    callback.onLoginSuccess(AuthViewModel.Social.KAKAO)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    Log.e("카카오", "로그인 실패 $error")
                    callback.onLoginFailure(error)
                } else if (token != null) {
                    //Log.e("카카오", "로그인 성공 ${token.accessToken}")
                    callback.onLoginSuccess(AuthViewModel.Social.KAKAO)
                }
            }
        }
    }

    fun googleSignIn(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            //Log.d("구글", "로그인 성공: ${account.idToken}")
            callback.onLoginSuccess(AuthViewModel.Social.GOOGLE)
        } catch (e: ApiException) {
            //Log.w("구글", "signInResult:failed code=" + e.statusCode)
            callback.onLoginFailure(e)
        }
    }
}
