package com.droidblossom.archive.presentation.ui.home.dialog

import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

interface CapsulePreviewDialogViewModel {

    val capsulePreviewDialogEvents: SharedFlow<CapsulePreviewDialogEvent>
    val secretCapsuleSummary : StateFlow<SecretCapsuleSummary>
    val startTime: StateFlow<Calendar?>
    val endTime: StateFlow<Calendar?>
    val totalTime: StateFlow<Int?>
    val timeProgress: StateFlow<Int?>
    val timerState: StateFlow<String>
    val capsuleOpenState: StateFlow<Boolean>
    val visibleCapsuleOpenMessage: StateFlow<Boolean>
    val capsuleTypeImage: StateFlow<Int>
    val visibleTimeProgressBar: StateFlow<Boolean>
    val visibleOpenProgressBar: StateFlow<Boolean>
    val calledFromCamera: StateFlow<Boolean>
    val timeCapsule: StateFlow<Boolean>

    fun capsulePreviewDialogEvent(event: CapsulePreviewDialogEvent)
    fun calculateCapsuleOpenTime(createdAt: String, dueDate: String)
    fun setProgressBar()

    fun setCapsuleTypeImage(image : Int)

    fun openCapsule(capsuleId : Long)
    fun setVisibleOpenProgressBar(visible: Boolean)

    fun setCalledFromCamera(calledFromCamera : Boolean)

    sealed class CapsulePreviewDialogEvent{
        data class ShowToastMessage(val message : String) : CapsulePreviewDialogEvent()

        object CapsuleOpenSuccess : CapsulePreviewDialogEvent()
        object MoveCapsuleDetail : CapsulePreviewDialogEvent()

    }
}