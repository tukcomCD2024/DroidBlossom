package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.base.BaseViewModel.Companion.throttleFirst
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import com.droidblossom.archive.presentation.model.mypage.GroupProfileData
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend.FriendDetailViewModel
import com.droidblossom.archive.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModelImpl @Inject constructor(

) : BaseViewModel(), GroupDetailViewModel {

    private val _groupDetailEvents = MutableSharedFlow<GroupDetailViewModel.GroupDetailEvent>()
    override val groupDetailEvents: SharedFlow<GroupDetailViewModel.GroupDetailEvent>
        get() = _groupDetailEvents.asSharedFlow()

    private val _capsules = MutableStateFlow(listOf<CapsuleData>())
    override val capsules: StateFlow<List<CapsuleData>>
        get() = _capsules

    private val _capsulesHasNextPage = MutableStateFlow(true)
    override val capsulesHasNextPage: StateFlow<Boolean>
        get() = _capsulesHasNextPage

    private val _capsulesLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val capsulesLastCreatedTime: StateFlow<String>
        get() = _capsulesLastCreatedTime

    private val _isShowMore = MutableStateFlow(false)
    override val isShowMore: StateFlow<Boolean>
        get() = _isShowMore

    private val capsuleScrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val capsuleScrollEventFlow = capsuleScrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getCapsuleListJob: Job? = null

    private val _groupInfo = MutableStateFlow(GroupProfileData(
        groupId = 7778,
        groupName = "Kathrine Turner",
        groupDescription = "추억을 소중하게 여기는 분들을 위한 AR 타임캡슐 앱 ARchive를 소개합니다.\n" +
                "이 앱으로 뜻깊은 순간들을 영원히 저장하세요.\n" +
                "이미지, 동영상, 텍스트를 활용해 타임캡슐을 생성하고, AR 카메라로 그 기억들을 다시 열어볼 수 있습니다..\n" +
                "지정된 시간이나 장소에 도착하면 알림이 울려, 친구들과 함께 개인적인 추억을 나누고 즐길 수 있습니다.\n" +
                "당신만의 매력적인 이미지로 움직이는 귀여운 캐릭터 타임캡슐 스킨을 직접 제작해보세요.\n" +
                "우리 앱으로 과거와 현재, 미래를 연결하는 특별한 경험을 즐기실 수 있습니다.",
        groupProfileUrl = "https://www.google.com/#q=quod",
        hasEditPermission = false,
        groupCapsuleNum = 7090,
        groupMemberNum = "5",
    ))

    override val groupInfo: StateFlow<GroupProfileData>
        get() = _groupInfo

    init {
        viewModelScope.launch {
            capsuleScrollEventFlow.collect{
                if (capsules.value.isEmpty()){
                    getLatestCapsuleList()
                }else{
                    getCapsuleList()
                }
            }
        }

        val capsules = mutableListOf<CapsuleData>()

        for (i in 0 .. 30){
            capsules.add(
                CapsuleData(
                    capsuleId = i.toLong(),
                    capsuleSkinUrl = "https://avatars.githubusercontent.com/u/69802523?v=4",
                    createdDate = "quaestio",
                    dueDate = null,
                    isOpened = false,
                    title = "malesuada",
                    capsuleType = "tempor"
                )
            )
        }

        _capsules.value = capsules
    }

    override fun groupDetailEvent(event: GroupDetailViewModel.GroupDetailEvent) {
        viewModelScope.launch {
            _groupDetailEvents.emit(event)
        }
    }

    override fun onCapsuleScrollNearBottom() {
        capsuleScrollEventChannel.trySend(Unit)
    }


    override fun getCapsuleList() {
        if (capsulesHasNextPage.value){
            getCapsuleListJob?.cancel()
            getCapsuleListJob = viewModelScope.launch {

            }
        }
    }

    override fun getLatestCapsuleList(){
        getCapsuleListJob?.cancel()
        getCapsuleListJob = viewModelScope.launch {

        }
    }

    override fun setShowMore() {
        _isShowMore.value = !_isShowMore.value
    }

}