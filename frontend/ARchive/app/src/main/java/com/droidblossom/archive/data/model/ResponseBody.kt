package com.droidblossom.archive.data.model

data class ResponseBody<T> (
    val code : Int,
    val message : String,
    val result : T
)