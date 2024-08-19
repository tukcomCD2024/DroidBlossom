package com.droidblossom.archive.presentation.ui.mypage.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.member.request.MemberDataRequestDto
import com.droidblossom.archive.domain.model.member.Announcement
import com.droidblossom.archive.domain.model.member.Announcements
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.usecase.auth.SignOutUseCase
import com.droidblossom.archive.domain.usecase.member.ChangePhoneSearchAvailableUseCase
import com.droidblossom.archive.domain.usecase.member.ChangeTagSearchAvailableUseCase
import com.droidblossom.archive.domain.usecase.member.DeleteAccountUseCase
import com.droidblossom.archive.domain.usecase.member.GetAnnouncementsUseCase
import com.droidblossom.archive.domain.usecase.member.MemberDataModifyUseCase
import com.droidblossom.archive.domain.usecase.member.MemberUseCase
import com.droidblossom.archive.domain.usecase.member.NotificationEnabledUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModelImpl @Inject constructor(
    private val notificationEnabledUseCase: NotificationEnabledUseCase,
    private val dataStoreUtils: DataStoreUtils,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val memberUseCase: MemberUseCase,
    private val memberModifyUseCase: MemberDataModifyUseCase,
    private val changeTagSearchAvailableUseCase: ChangeTagSearchAvailableUseCase,
    private val changePhoneSearchAvailableUseCase: ChangePhoneSearchAvailableUseCase,
    private val getAnnouncementsUseCase: GetAnnouncementsUseCase
) : BaseViewModel(), SettingViewModel {

    //main
    private val _settingMainEvents = MutableSharedFlow<SettingViewModel.SettingMainEvent>()
    override val settingMainEvents: SharedFlow<SettingViewModel.SettingMainEvent>
        get() = _settingMainEvents.asSharedFlow()
    private val _isOnlyProfile = MutableStateFlow(false)
    override val isOnlyProfile: StateFlow<Boolean>
        get() = _isOnlyProfile

    private val _announcements = MutableStateFlow(listOf<Announcement>())
    override val announcements: StateFlow<List<Announcement>>
        get() = _announcements

    //notification
    private val _notificationEnable = MutableStateFlow<Boolean>(false)
    override val notificationEnable: StateFlow<Boolean>
        get() = _notificationEnable

    private val _settingNotificationEvent =
        MutableSharedFlow<SettingViewModel.SettingNotificationEvent>()
    override val settingNotificationEvents: SharedFlow<SettingViewModel.SettingNotificationEvent>
        get() = _settingNotificationEvent.asSharedFlow()

    private val _settingUserEvent =
        MutableSharedFlow<SettingViewModel.SettingUserEvent>()
    override val settingUserEvents: SharedFlow<SettingViewModel.SettingUserEvent>
        get() = _settingUserEvent.asSharedFlow()

    private val _myInfo = MutableStateFlow(
        MemberDetail(
            "USER", "", "", "", "", "", 0, 0,
            tagSearchAvailable = false,
            phoneSearchAvailable = false
        )
    )
    override val myInfo: StateFlow<MemberDetail>
        get() = _myInfo
    override val modifyNameText = MutableStateFlow<String>("")
    override val modifyTagText = MutableStateFlow<String>("")
    override val isTagDuplication = MutableStateFlow<Boolean>(false)

    init {
        getMe()
    }

    override fun back() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.Back)
        }
    }

    override fun goUser() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoUser)
        }
    }

    override fun goNotification() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoNotification)
        }
    }

    override fun goNotice() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoNotice)
        }
    }

    override fun goAgree() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoAgree)
        }
    }

    override fun goInquire() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoInquire)
        }
    }

    override fun goPrivacyPolicy() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoPrivacyPolicy)
        }
    }

    override fun goLicenses() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoLicenses)
        }
    }

    override fun goLogout() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoLogout)
        }
    }

    fun onlyProfile() {
        viewModelScope.launch {
            _isOnlyProfile.emit(true)
        }
    }

    override fun goDeleteAccount() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoDeleteAccount)
        }
    }

    override fun singOutRequest() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.ShowLoading)
            signOutUseCase().collect { result ->
                result.onSuccess {
                    dataStoreUtils.resetTokenData()
                    _settingMainEvents.emit(SettingViewModel.SettingMainEvent.ShowToastMessage("성공적으로 로그아웃되었습니다."))
                    _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoAuthActivity)
                }.onFail {
                    _settingMainEvents.emit(SettingViewModel.SettingMainEvent.ShowToastMessage("로그아웃을 실패했습니다. 잠시 후 다시 시도해 주세요."))
                }
            }
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.DismissLoading)
        }
    }

    override fun deleteAccountRequest() {
        viewModelScope.launch {
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.ShowLoading)
            deleteAccountUseCase().collect { result ->
                result.onSuccess {
                    dataStoreUtils.resetTokenData()
                    _settingMainEvents.emit(SettingViewModel.SettingMainEvent.ShowToastMessage("성공적으로 탈퇴되었습니다. 다음에 또 ARchive를 찾아주세요!"))
                    _settingMainEvents.emit(SettingViewModel.SettingMainEvent.GoAuthActivity)
                }.onFail {
                    _settingMainEvents.emit(SettingViewModel.SettingMainEvent.ShowToastMessage("계정 탈퇴에 실패했습니다. 잠시 후 다시 시도해 주세요."))
                }
            }
            _settingMainEvents.emit(SettingViewModel.SettingMainEvent.DismissLoading)
        }
    }

    private fun goAuth() {

    }

    //Notification Setting
    fun getNotificationEnable() {
        viewModelScope.launch {
            _notificationEnable.emit(dataStoreUtils.fetchNotificationsEnabled())
        }
    }

    fun postNotificationEnable(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled xor notificationEnable.value) {
                dataStoreUtils.saveNotificationsEnabled(enabled)
                notificationEnabledUseCase(enabled).collect { result ->
                    result.onSuccess {
                        _settingNotificationEvent.emit(
                            SettingViewModel.SettingNotificationEvent.ShowToastMessage(
                                "알림 설정을 변경했습니다."
                            )
                        )
                    }.onFail {
                        _settingNotificationEvent.emit(
                            SettingViewModel.SettingNotificationEvent.ShowToastMessage(
                                "알림 설정을 실패했습니다. 잠시 후 다시 시도해 주세요."
                            )
                        )

                    }
                }
                _settingNotificationEvent.emit(SettingViewModel.SettingNotificationEvent.Back)
            } else {
                _settingNotificationEvent.emit(SettingViewModel.SettingNotificationEvent.Back)
            }
        }
    }

    fun getMe() {
        viewModelScope.launch {
            memberUseCase().collect { result ->
                result.onSuccess {
                    _myInfo.emit(it)
                    modifyNameText.emit(it.nickname)
                    modifyTagText.emit(it.tag)
                }.onFail {
                    _settingNotificationEvent.emit(
                        SettingViewModel.SettingNotificationEvent.ShowToastMessage(
                            "정보를 불러오는데 실패했습니다. 잠시 후 다시 시도해 주세요."
                        )
                    )
                }
            }
        }
    }

    override fun modifyMe() {
        viewModelScope.launch {
            memberModifyUseCase(
                MemberDataRequestDto(
                    modifyNameText.value,
                    modifyTagText.value
                )
            ).collect { result ->
                result.onSuccess {
                    _myInfo.emit(
                        myInfo.value.copy(
                            nickname = modifyNameText.value,
                            tag = modifyTagText.value,
                        )
                    )
                    _settingUserEvent.emit(SettingViewModel.SettingUserEvent.Back)
                }.onFail {
                    isTagDuplication.emit(true)
                    _settingUserEvent.emit(
                        SettingViewModel.SettingUserEvent.ShowToastMessage(
                            "정보 수정에 실패했습니다. 잠시 후 다시 시도해 주세요."
                        )
                    )
                }
            }
        }
    }

    fun changeTagSearch() {
        viewModelScope.launch {
            changeTagSearchAvailableUseCase(!myInfo.value.tagSearchAvailable).collect { result ->
                result.onSuccess {
                    if (!myInfo.value.tagSearchAvailable) {
                        _settingUserEvent.emit(
                            SettingViewModel.SettingUserEvent.ShowToastMessage(
                                "테그 검색을 허용합니다."
                            )
                        )
                    } else {
                        _settingUserEvent.emit(
                            SettingViewModel.SettingUserEvent.ShowToastMessage(
                                "테그 검색을 허용하지 않습니다."
                            )
                        )
                    }
                }.onFail {
                    _settingUserEvent.emit(
                        SettingViewModel.SettingUserEvent.ShowToastMessage(
                            "태그 검색 설정을 실패했습니다. 잠시 후 다시 시도해 주세요."
                        )
                    )

                }
            }
        }
    }

    fun changePhoneSearchV() {
        viewModelScope.launch {
            changePhoneSearchAvailableUseCase(!myInfo.value.phoneSearchAvailable).collect { result ->
                result.onSuccess {
                    if (!myInfo.value.phoneSearchAvailable) {
                        _settingUserEvent.emit(
                            SettingViewModel.SettingUserEvent.ShowToastMessage(
                                "전화번호 검색을 허용합니다."
                            )
                        )
                    } else {
                        _settingUserEvent.emit(
                            SettingViewModel.SettingUserEvent.ShowToastMessage(
                                "전화번호 검색을 허용하지 않습니다."
                            )
                        )
                    }
                }.onFail {
                    _settingUserEvent.emit(
                        SettingViewModel.SettingUserEvent.ShowToastMessage(
                            "전화번호 검색 설정을 실패했습니다. 잠시 후 다시 시도해 주세요."
                        )
                    )

                }
            }
        }
    }

    fun getAnnouncements() {
        viewModelScope.launch {
            getAnnouncementsUseCase().collect { result ->
                result.onSuccess {
                    _announcements.emit(
                        it.announcements
                    )
                }.onFail {
                    _settingUserEvent.emit(
                        SettingViewModel.SettingUserEvent.ShowToastMessage(
                            "공지사항을 불러오는데 실패했습니다. 잠시 후 다시 시도해 주세요."
                        )
                    )
                }
            }
        }
    }
}