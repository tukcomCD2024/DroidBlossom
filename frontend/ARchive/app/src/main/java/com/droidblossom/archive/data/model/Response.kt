package com.droidblossom.archive.data.model

import com.droidblossom.archive.R

data class Response<T> (
    val code : Int,
    val message : String,
    val result : T
)