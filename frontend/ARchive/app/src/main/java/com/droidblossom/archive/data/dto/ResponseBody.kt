package com.droidblossom.archive.data.dto

data class ResponseBody<T> (
    val code : String,
    val message : String,
    val result : T
)