package com.droidblossom.archive.domain.model.secret

data class SecretCapsulePage(
    val capsules: List<SecretCapsuleSummary>,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
