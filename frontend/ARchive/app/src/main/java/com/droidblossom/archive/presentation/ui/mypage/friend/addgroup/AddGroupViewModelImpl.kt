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
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _checkedList = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val checkedList: StateFlow<List<FriendsSearchResponse>>
        get() = _checkedList

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
                        15,
                        friendLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        if (friendListUI.value.isEmpty()) {
                            _friendListUI.emit(it.friends)
                        } else {
                            _friendListUI.emit(_friendListUI.value + it.friends)
                        }
                        friendLastCreatedTime.value = it.friends.last().createdAt
                    }.onFail {
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                                "친구 리스트 불러오기 실패. 잠시후 시도해 주세요"
                            )
                        )
                    }
                }
            }
        }
    }

    fun checkFriendList(position: Int) {
        viewModelScope.launch {
            val newAddList = friendListUI.value
            if (newAddList[position].isChecked) {
                newAddList[position].isChecked = false
                _checkedList.emit(newAddList.filter { it.isChecked })
            } else {
                newAddList[position].isChecked = true
                _checkedList.emit(newAddList.filter { it.isChecked })
            }
            _friendListUI.emit(newAddList)
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
                    //skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.DismissLoading)
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
                Log.d("addgroup", "S3 사진 업로드 실패")
                //skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.DismissLoading)
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
                    _addGroupEvent.emit(AddGroupViewModel.AddGroupEvent.Finish)
                    Log.d("addgroup", "그룹 생성 성공")
                }.onFail {
                    _addGroupEvent.emit(AddGroupViewModel.AddGroupEvent.ShowToastMessage("서버와 연결을 실패했습니다."))
                    Log.d("addgroup", "그룹 생성 실패")

                }
            }
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