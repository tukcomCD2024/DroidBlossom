package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group

import com.droidblossom.archive.domain.model.group.GroupMember
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import com.droidblossom.archive.presentation.model.mypage.detail.GroupProfileData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface GroupDetailViewModel {

    val groupId: StateFlow<Long>
    val groupInfo: StateFlow<GroupProfileData>
    val groupDetailEvents: SharedFlow<GroupDetailEvent>
    val groupMembers: StateFlow<List<GroupMember>>
    val capsules : StateFlow<List<CapsuleData>>
    val capsulesHasNextPage : StateFlow<Boolean>
    val isShowMore:StateFlow<Boolean>
    val isAppBarExpanded:StateFlow<Boolean>

    fun groupDetailEvent(event: GroupDetailEvent)

    fun onCapsuleScrollNearBottom()

    fun setGroupId(groupId: Long)
    fun getGroupDetail()

    fun getCapsuleList()
    fun getLatestCapsuleList()
    fun setShowMore()
    fun setIsAppBarExpanded(boolean: Boolean)
    fun closureGroup()

    fun leaveGroup()
    fun updateCapsuleOpenState(capsuleIndex: Int, capsuleId: Long)

    fun deleteCapsule(capsuleIndex: Int, capsuleId: Long)


    sealed class GroupDetailEvent{
        object SwipeRefreshLayoutDismissLoading : GroupDetailEvent()
        object LeaveGroupSuccess: GroupDetailEvent()
        data class ShowToastMessage(val message : String) : GroupDetailEvent()
        object SuccessClosureGroup: GroupDetailEvent()

        object DismissLoading : GroupDetailEvent()
        object ShowLoading : GroupDetailEvent()

    }
}