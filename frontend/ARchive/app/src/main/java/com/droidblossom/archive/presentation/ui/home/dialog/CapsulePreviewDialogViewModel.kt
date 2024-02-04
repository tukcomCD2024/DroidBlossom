package com.droidblossom.archive.presentation.ui.home.dialog

import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

interface CapsulePreviewDialogViewModel {

    val secretCapsuleSummary : StateFlow<SecretCapsuleSummary>
    val startTime: StateFlow<Calendar?>
    val endTime: StateFlow<Calendar?>
    val totalTime: StateFlow<Int?>
    val initialProgress: StateFlow<Int?>
    val timerState: StateFlow<String>
    val visibleCapsuleOpenMessage: StateFlow<Boolean>

    fun calculateCapsuleOpenTime(createdAt: String, dueDate: String)
    fun setProgressBar()
}