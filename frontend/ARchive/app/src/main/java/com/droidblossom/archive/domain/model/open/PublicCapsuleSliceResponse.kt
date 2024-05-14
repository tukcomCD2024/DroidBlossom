package com.droidblossom.archive.domain.model.open

import com.droidblossom.archive.domain.model.common.SocialCapsules

data class PublicCapsuleSliceResponse (
    val publicCapsules: List<SocialCapsules>,
    val hasNext: Boolean
)