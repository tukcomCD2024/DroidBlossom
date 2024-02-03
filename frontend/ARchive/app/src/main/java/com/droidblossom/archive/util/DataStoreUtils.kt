package com.droidblossom.archive.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class DataStoreUtils @Inject constructor(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("AccessToken")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("RefreshToken")
    }

    suspend fun saveAccessToken(accessToken: String) {
        val (iv, encryptedData) = EncryptionUtils.encrypt(accessToken)
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encryptedData.toBase64() + ":" + iv.toBase64()
        }
    }

    suspend fun fetchAccessToken(): String {
        val preferences = context.dataStore.data.first()
        val combined = preferences[ACCESS_TOKEN_KEY] ?: return ""
        val (encryptedData, iv) = combined.split(":").map { it.fromBase64() }
        return EncryptionUtils.decrypt(iv, encryptedData)
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        val (iv, encryptedData) = EncryptionUtils.encrypt(refreshToken)
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = encryptedData.toBase64() + ":" + iv.toBase64()
        }
    }

    suspend fun fetchRefreshToken(): String {
        val preferences = context.dataStore.data.first()
        val combined = preferences[REFRESH_TOKEN_KEY] ?: return ""
        val (encryptedData, iv) = combined.split(":").map { it.fromBase64() }
        return EncryptionUtils.decrypt(iv, encryptedData)
    }

    suspend fun resetTokenData() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}

private fun ByteArray.toBase64(): String = android.util.Base64.encodeToString(this, android.util.Base64.NO_WRAP)
private fun String.fromBase64(): ByteArray = android.util.Base64.decode(this, android.util.Base64.NO_WRAP)