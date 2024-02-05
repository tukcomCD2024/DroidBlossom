package com.droidblossom.archive.presentation.ui.capsule

import com.droidblossom.archive.domain.model.secret.SecretCapsuleDetail
import kotlinx.coroutines.flow.StateFlow

interface CapsuleDetailViewModel {
    val capsuleDetail :StateFlow<SecretCapsuleDetail>
    fun getSecretCapsuleDetail(id:Long)
}