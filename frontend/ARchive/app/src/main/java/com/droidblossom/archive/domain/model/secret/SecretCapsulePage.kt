package com.droidblossom.archive.domain.model.secret

data class SecretCapsulePage(
    val capsules: List<SecretCapsuleSummery>,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
