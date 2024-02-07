package com.droidblossom.archive.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtils {
    private const val KEY_ALIAS = "MyKeyAlias"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val TAG_LENGTH = 128

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    init {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateSecretKey(KEY_ALIAS)
        }
    }

    private fun generateSecretKey(alias: String) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun encrypt(data: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray())
        return Pair(iv, encryptedData)
    }

    fun decrypt(iv: ByteArray, encryptedData: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        val spec = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decryptedData = cipher.doFinal(encryptedData)
        return String(decryptedData)
    }
}