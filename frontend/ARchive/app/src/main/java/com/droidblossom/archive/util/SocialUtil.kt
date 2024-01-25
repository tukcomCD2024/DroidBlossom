package com.droidblossom.archive.util

import com.droidblossom.archive.presentation.ui.auth.AuthViewModel

object SocialUtils {

    // enum을 문자열로 변환
    fun enumToString(social: AuthViewModel.Social): String {
        return social.name
    }

    // 문자열을 enum으로 변환
    fun stringToEnum(socialString: String): AuthViewModel.Social {
        return AuthViewModel.Social.valueOf(socialString)
    }
}