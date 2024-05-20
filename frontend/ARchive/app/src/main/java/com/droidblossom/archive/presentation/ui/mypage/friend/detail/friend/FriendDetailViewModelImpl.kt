package com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import com.droidblossom.archive.presentation.model.mypage.FriendProfileData
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
class FriendDetailViewModelImpl @Inject constructor(

) : BaseViewModel(), FriendDetailViewModel {
    
    private val _friendDetailEvents = MutableSharedFlow<FriendDetailViewModel.FriendDetailEvent>()
    override val friendDetailEvents: SharedFlow<FriendDetailViewModel.FriendDetailEvent>
        get() = _friendDetailEvents.asSharedFlow()

    private val _friendInfo = MutableStateFlow(FriendProfileData(
        profileId = 6740,
        nickname = "Gerard Woodard",
        profileUrl = "https://search.yahoo.com/search?p=commodo",
        tag = "cetero"
    ))
    override val friendInfo: StateFlow<FriendProfileData>
        get() = _friendInfo


    private val _capsules = MutableStateFlow(listOf<CapsuleData>())
    override val capsules: StateFlow<List<CapsuleData>>
        get() = _capsules

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow = scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getCapsuleListJob: Job? = null

    init {
        viewModelScope.launch {
            scrollEventFlow.collect{
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

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }


    override fun friendDetailEvent(event: FriendDetailViewModel.FriendDetailEvent) {
        viewModelScope.launch {
            _friendDetailEvents.emit(event)
        }
    }

    override fun getCapsuleList() {
        if (hasNextPage.value){
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

}