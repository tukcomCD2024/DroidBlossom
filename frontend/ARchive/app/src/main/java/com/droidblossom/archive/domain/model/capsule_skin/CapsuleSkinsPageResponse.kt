package com.droidblossom.archive.domain.model.capsule_skin

import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary

data class CapsuleSkinsPageResponse(
    val skins : List<CapsuleSkinSummary>,
    val hasNext : Boolean
)
