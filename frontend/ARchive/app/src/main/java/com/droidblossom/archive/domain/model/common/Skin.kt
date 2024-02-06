package com.droidblossom.archive.domain.model.common

import android.net.Uri

data class Skin(
    val skinId: Int,
    val skinUrl : String,
    val name : String,
    val createdAt : String,
    var isClicked :Boolean
)
