package com.droidblossom.archive.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class SharedPreferencesUtils(context: Context) {

    val masterKeyAlias = MasterKey
        .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val pref = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveAccessToken(accessToken: String) {
        pref.edit().putString("AccessToken", accessToken).apply()
    }

    fun fetchAccessToken(): String {
        return pref.getString("AccessToken", "") ?: ""
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        pref.edit().putString("RefreshToken", refreshToken).apply()
    }

    fun fetchRefreshToken(): String {
        return pref.getString("RefreshToken", "") ?: ""
    }

    fun resetTokenData(){
        pref.edit().apply {
            remove("AccessToken")
            remove("RefreshToken")
            apply()
        }
    }

    companion object {
        private const val FILE_NAME = "ARchive_encrypted_pref_file"
    }
}