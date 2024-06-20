package com.droidblossom.archive.domain.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CapsuleSkinSummary (
    val id : Long,
    val skinUrl : String,
    val name : String,
    val createdAt : String,
    var isClicked :Boolean
): Parcelable