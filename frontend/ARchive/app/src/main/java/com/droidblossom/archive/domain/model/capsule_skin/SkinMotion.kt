package com.droidblossom.archive.domain.model.capsule_skin

import com.droidblossom.archive.util.Motion
import com.droidblossom.archive.util.Retarget

data class SkinMotion(
    val id : Long,
    val motionUrl: String,
    val motionName: Motion,
    val retarget: Retarget,
    var isClicked :Boolean
)
