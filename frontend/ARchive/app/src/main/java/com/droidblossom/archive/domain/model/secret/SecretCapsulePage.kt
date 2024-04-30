package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.presentation.model.mypage.CapsuleData

data class SecretCapsulePage(
    val capsules: List<CapsuleData>,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
