package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.presentation.model.mypage.CapsuleData

data class CapsulePageList(
    val capsules: List<CapsuleData>,
    val hasNext: Boolean
)
