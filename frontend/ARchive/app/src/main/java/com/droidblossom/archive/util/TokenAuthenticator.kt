package com.droidblossom.archive.util

import android.content.Intent
import android.util.Log
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.BuildConfig
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.domain.model.auth.TokenReIssue
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val ds: DataStoreUtils,
    private val authServiceLazy: Lazy<AuthService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val isPathRefresh =
            response.request.url.toString() == BuildConfig.BASE_URL + BuildConfig.REISSUETOKEN

        val refreshToken = runBlocking { ds.fetchRefreshToken() }
        if (refreshToken.isEmpty()) return null

        if (response.code == 400) {
            runBlocking { ds.resetTokenData() }
            redirectToLogin()
            return null
        }


        if (response.code == 401 && !isPathRefresh) {

            return if (fetchUpdateToken()) {
                val newToken = runBlocking { ds.fetchAccessToken() }
                response.request.newBuilder().apply {
                    removeHeader("Authorization")
                    addHeader("Authorization", "Bearer $newToken")
                }.build()
            } else {
                runBlocking { ds.resetTokenData() }
                redirectToLogin()
                null
            }
        }
        return null
    }

    private fun fetchUpdateToken(): Boolean = runBlocking {
        val authService = authServiceLazy.get()
        val refreshToken = ds.fetchRefreshToken()
        try {
            val response = authService.postReIssueApi(TokenReIssue(refreshToken).toDto())
            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                ds.saveAccessToken(responseBody.result.accessToken)
                ds.saveRefreshToken(responseBody.result.refreshToken)
                return@runBlocking true
            } else if (response.code() == 400) {
                return@runBlocking false
            }
        } catch (e: Exception) {
            // 예외 처리
        }
        return@runBlocking false
    }

    private fun redirectToLogin(){
        Log.d("토큰", "리다이렉션")
        val intent = Intent(ARchiveApplication.getContext(), AuthActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ARchiveApplication.getContext().startActivity(intent)
    }
}