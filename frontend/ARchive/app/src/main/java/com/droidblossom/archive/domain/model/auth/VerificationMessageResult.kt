package com.droidblossom.archive.domain.model.auth

data class VerificationMessageResult (
    val status : String,
    val message : String,
    val field : String,
    val value : String,
    val reason : String
)