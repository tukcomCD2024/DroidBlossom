package com.droidblossom.archive.util

import com.droidblossom.archive.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val ds: DataStoreUtils
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val isPathRefresh =
            response.request.url.toString() == BuildConfig.BASE_URL + "토큰 갱신 url"

        if (response.code == 401 && !isPathRefresh) {

            return if (fetchUpdateToken()) {
                val newToken = runBlocking { ds.fetchAccessToken() }
                // UnAuthorized 예외가 발생한 요청을 복제하여 다시 요청합니다.
                response.request.newBuilder().apply {
                    removeHeader("Authorization")
                    addHeader("Authorization", "Bearer $newToken")
                }.build()
            } else {
                // RefreshToken도 만료되어 로그인이 다시 필요한 상황입니다.
                // ex. 로그인 화면으로 이동
                null
            }
        }
        return null
    }

    private fun fetchUpdateToken(): Boolean {
        val request = runBlocking {
            // 토큰 갱신 API 호출, 로컬 저장
        }
        return false
    }
}