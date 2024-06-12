package com.droidblossom.archive.presentation.ui.home.dialog

import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

interface CapsulePreviewDialogViewModel {

    val capsulePreviewDialogEvents: SharedFlow<CapsulePreviewDialogEvent>
    val capsuleSummaryResponse : StateFlow<CapsuleSummaryResponse>
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
    val canOpenCapsule: StateFlow<Boolean>
    val capsuleType: StateFlow<HomeFragment.CapsuleType>
    val groupCapsuleMembers: StateFlow<List<GroupCapsuleMember>>
    val capsuleId: StateFlow<Long>
    val myGroupCapsuleOpenStatus: StateFlow<Boolean>

    fun setCapsuleId(capsuleId:Long)
    fun capsulePreviewDialogEvent(event: CapsulePreviewDialogEvent)
    fun calculateCapsuleOpenTime(createdAt: String, dueDate: String)
    fun setProgressBar()

    fun setCapsuleTypeImage(image : Int, type: HomeFragment.CapsuleType)

    fun openCapsule()
    fun setVisibleOpenProgressBar(visible: Boolean)

    fun setCalledFromCamera(calledFromCamera : Boolean)

    fun getSecretCapsuleSummary()
    fun getPublicCapsuleSummary()
    fun getGroupCapsuleSummary()
    fun getGroupMembersCapsuleOpenStatus()

    fun getTreasureCapsuleSummary()

    fun setGroupCapsuleOpenState()

    sealed class CapsulePreviewDialogEvent{
        data class ShowToastMessage(val message : String) : CapsulePreviewDialogEvent()

        object CapsuleOpenSuccess : CapsulePreviewDialogEvent()
        object MoveCapsuleDetail : CapsulePreviewDialogEvent()
        object DismissCapsulePreviewDialog : CapsulePreviewDialogEvent()

    }
}