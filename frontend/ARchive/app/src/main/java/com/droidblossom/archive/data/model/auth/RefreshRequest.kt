package com.droidblossom.archive.data.model.auth

import com.droidblossom.archive.data.model.Response

data class RefreshRequest(
    val refreshToken: String,
)