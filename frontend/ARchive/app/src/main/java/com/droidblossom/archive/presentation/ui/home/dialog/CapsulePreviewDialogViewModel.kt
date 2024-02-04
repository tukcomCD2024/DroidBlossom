package com.droidblossom.archive.presentation.ui.home.dialog

import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary
import kotlinx.coroutines.flow.StateFlow

interface CapsulePreviewDialogViewModel {

    val secretCapsuleSummary : StateFlow<SecretCapsuleSummary>
}