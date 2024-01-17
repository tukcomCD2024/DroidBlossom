package com.droidblossom.archive.util

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
        fun onLoginSuccess(authId : String, email : String, profileUrl : String,  social: AuthViewModel.Social)
        fun onLoginFailure(error: Throwable)
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
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
                    kakaoGetUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    Log.e("카카오", "로그인 실패 $error")
                    callback.onLoginFailure(error)
                } else if (token != null) {
                    //Log.e("카카오", "로그인 성공 ${token.accessToken}")
                    kakaoGetUserInfo()
                }
            }
        }
    }

    private fun kakaoGetUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("카카오", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val authId = user.id.toString()
                val email = user.kakaoAccount?.email ?: ""
                val profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""
                callback.onLoginSuccess(authId, email, profileUrl, AuthViewModel.Social.KAKAO)
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
            Log.d("구글 id", "로그인 성공: ${account.id}")
            Log.d("구글 email", "로그인 성공: ${account.email}")
            Log.d("구글 프로필", "로그인 성공: ${account.photoUrl}")
            //callback.onLoginSuccess(AuthViewModel.Social.GOOGLE)
        } catch (e: ApiException) {
            //Log.w("구글", "signInResult:failed code=" + e.statusCode)
            //callback.onLoginFailure(e)
        }
    }
}
