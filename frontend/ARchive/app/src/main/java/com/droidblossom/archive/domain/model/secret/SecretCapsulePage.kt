package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.domain.model.common.MyCapsule

data class SecretCapsulePage(
    val capsules: List<MyCapsule>,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
