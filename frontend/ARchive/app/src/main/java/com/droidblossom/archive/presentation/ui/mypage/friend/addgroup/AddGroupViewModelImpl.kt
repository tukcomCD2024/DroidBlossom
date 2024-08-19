package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.data.dto.s3.request.S3OneUrlRequestDto
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsMakeRequest
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.domain.usecase.friend.FriendsForAddGroupPageUseCase
import com.droidblossom.archive.domain.usecase.group.GroupCreateUseCase
import com.droidblossom.archive.domain.usecase.s3.S3OneUrlGetUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModel
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModelImpl
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.FileUtils
import com.droidblossom.archive.util.S3Util
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModelImpl @Inject constructor(
    private val s3OneUrlGetUseCase: S3OneUrlGetUseCase,
    private val friendsForAddGroupPageUseCase: FriendsForAddGroupPageUseCase,
    private val groupCreateUseCase: GroupCreateUseCase,
    private val s3Util: S3Util,
) : BaseViewModel(), AddGroupViewModel {

    private val _addGroupEvent = MutableSharedFlow<AddGroupViewModel.AddGroupEvent>()
    override val addGroupEvent: SharedFlow<AddGroupViewModel.AddGroupEvent>
        get() = _addGroupEvent.asSharedFlow()
    override val groupTitle: MutableStateFlow<String> = MutableStateFlow("")
    override val groupContent: MutableStateFlow<String> = MutableStateFlow("")
    override val groupProfileUri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    override val searchFriendText: MutableStateFlow<String> = MutableStateFlow("")
    private val _profileImgFile = MutableStateFlow<File?>(null)
    private val profileImgFile: StateFlow<File?>
        get() = _profileImgFile

    private val _isCollapse = MutableStateFlow<Boolean>(false)
    override val isCollapse: MutableStateFlow<Boolean>
        get() = _isCollapse

    private val _isFriendSearchOpen = MutableStateFlow(false)
    override val isFriendSearchOpen: StateFlow<Boolean>
        get() = _isFriendSearchOpen

    private val _friendListUI = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val friendListUI: StateFlow<List<FriendsSearchResponse>>
        get() = _friendListUI

    private val _friendList = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val friendList: StateFlow<List<FriendsSearchResponse>>
        get() = _friendList

    private val _checkedList = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val checkedList: StateFlow<List<FriendsSearchResponse>>
        get() = _checkedList

    private val _notifyItemChangedPosition = MutableSharedFlow<Int>(0)

    val notifyItemChangedPosition
        get() = _notifyItemChangedPosition.asSharedFlow()

    private val friendHasNextPage = MutableStateFlow(true)

    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)


    private val scrollFriendEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollFriendEventFlow =
        scrollFriendEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getFriendLstJob: Job? = null

    init {
        viewModelScope.launch {
            scrollFriendEventFlow.collect {
                getFriendList()
            }
        }
    }

    fun onScrollNearBottomFriend() {
        scrollFriendEventChannel.trySend(Unit)
    }

    override fun getFriendList() {
        if (friendHasNextPage.value) {
            getFriendLstJob?.cancel()
            getFriendLstJob = viewModelScope.launch {
                friendsForAddGroupPageUseCase(
                    PagingRequestDto(
                        999,
                        friendLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        if (friendListUI.value.isEmpty()) {
                            _friendList.emit(it.friends)
                            _friendListUI.emit(it.friends)
                        } else {
                            _friendList.emit(_friendList.value + it.friends)
                            _friendListUI.emit(_friendListUI.value + it.friends)
                        }
                        friendLastCreatedTime.value = it.friends.last().createdAt
                    }.onFail {
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                                "친구 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."
                            )
                        )
                    }
                }
            }
        }
    }

    fun searchFriend() {
        viewModelScope.launch {
            if (searchFriendText.value.isBlank()) {
                _friendListUI.emit(friendList.value)
            } else {
                _friendListUI.emit(friendList.value.filter { it.nickname.contains(searchFriendText.value) })
            }
        }
    }

    fun checkFriendList(position: Int) {
        viewModelScope.launch {
            val newUIList = friendListUI.value
            val newCheckedList = checkedList.value.toMutableList()
            if (newUIList[position].isChecked) {
                newUIList[position].isChecked = false
                newCheckedList.remove(newUIList[position])
            } else {
                newUIList[position].isChecked = true
                newCheckedList.add(newUIList[position])
            }
            _checkedList.emit(newCheckedList)
            _friendListUI.emit(newUIList)
        }
    }

    fun onCloseChip(friend: FriendsSearchResponse) {
        viewModelScope.launch {
            val newCheckList = checkedList.value.toMutableList()
            newCheckList.remove(friend)
            _checkedList.emit(newCheckList)
            friendList.value.indexOf(friend).let {
                friendList.value[it].isChecked = false
            }
            if (friendListUI.value.contains(friend)) {
                friendListUI.value.indexOf(friend).let {
                    friendListUI.value[it].isChecked = false
                    _notifyItemChangedPosition.emit(it)
                }
            }
        }
    }

    fun setFile(profileImgFile: File) {
        _profileImgFile.value = profileImgFile
    }

    fun onCreateGroup() {
        viewModelScope.launch {
            getUploadUrls(S3OneUrlRequestDto(profileImgFile.value!!.name, S3DIRECTORY))
        }
    }

    private fun getUploadUrls(getS3UrlData: S3OneUrlRequestDto) {
        viewModelScope.launch {
            s3OneUrlGetUseCase(getS3UrlData).collect { result ->
                result.onSuccess {
                    Log.d("addgroup", "S3 URL : $it")
                    uploadFilesToS3(profileImgFile.value!!, it)
                }.onFail {
                    _addGroupEvent.emit(
                        AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                            "그룹 생성에 실패했습니다. 잠시 후 다시 시도해주세요."
                        )
                    )
                    _addGroupEvent.emit(
                        AddGroupViewModel.AddGroupEvent.DismissLoading
                    )
                    Log.d("addgroup", "S3 URL 받기 실패")
                }
            }
        }
    }

    private fun uploadFilesToS3(
        skinImgFiles: File,
        preSignedImageUrls: String,
    ) {
        viewModelScope.launch {

            val uploadSuccess = try {
                s3Util.uploadImageWithPresignedUrl(skinImgFiles, preSignedImageUrls)
                true
            } catch (e: Exception) {
                false
            }

            if (uploadSuccess) {
                Log.d("addgroup", "S3 사진 업로드 성공")
                createGroup()
            } else {
                _addGroupEvent.emit(
                    AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                        "그룹 생성에 실패했습니다. 잠시 후 다시 시도해주세요."
                    )
                )
                _addGroupEvent.emit(
                    AddGroupViewModel.AddGroupEvent.DismissLoading
                )
                Log.d("addgroup", "S3 사진 업로드 실패")
            }
        }
    }

    private fun createGroup() {
        viewModelScope.launch {
            groupCreateUseCase(
                CreateGroupRequestDto(
                    description = groupContent.value,
                    groupDirectory = S3DIRECTORY,
                    groupImage = profileImgFile.value!!.name,
                    groupName = groupTitle.value,
                    targetIds = checkedList.value.map {
                        it.id
                    }
                )
            ).collect { result ->
                result.onSuccess {
                    _addGroupEvent.emit(
                        AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                            "그룹이 생성되었습니다."
                        )
                    )
                    _addGroupEvent.emit(AddGroupViewModel.AddGroupEvent.Finish)
                }.onFail {
                    if (it == 500){
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                                "그룹 생성에 실패했습니다. 잠시 후 다시 시도해주세요."
                            )
                        )
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.Finish
                        )
                    }else{
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                                "그룹 생성에 실패했습니다. 잠시 후 다시 시도해주세요."
                            )
                        )
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.Finish
                        )
                    }
                }
            }
            _addGroupEvent.emit(AddGroupViewModel.AddGroupEvent.DismissLoading)
        }
    }

    override fun expandedAppBar() {
        viewModelScope.launch {
            _isCollapse.emit(false)
        }
    }

    override fun collapsedAppBar() {
        viewModelScope.launch {
            _isCollapse.emit(true)
        }
    }

    override fun search() {

    }

    override fun openSearch() {
        viewModelScope.launch {
            _isFriendSearchOpen.emit(true)
        }
    }

    override fun closeSearch() {
        viewModelScope.launch {
            _isFriendSearchOpen.emit(true)
        }
    }

    override fun onFinish() {
        viewModelScope.launch {
            _addGroupEvent.emit(AddGroupViewModel.AddGroupEvent.Finish)
        }
    }

    companion object {
        const val S3DIRECTORY = "groupProfile"
    }
}