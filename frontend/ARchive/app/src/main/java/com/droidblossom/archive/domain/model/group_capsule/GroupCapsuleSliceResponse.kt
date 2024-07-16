package com.droidblossom.archive.domain.model.group_capsule

import com.droidblossom.archive.domain.model.common.SocialCapsules

data class GroupCapsuleSliceResponse (
    val groupCapsules: List<SocialCapsules>,
    val hasNext: Boolean
)