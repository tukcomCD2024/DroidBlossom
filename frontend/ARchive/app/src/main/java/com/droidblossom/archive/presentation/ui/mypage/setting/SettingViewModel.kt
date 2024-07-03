package com.droidblossom.archive.presentation.ui.mypage.setting

import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SettingViewModel {

    val settingMainEvents: SharedFlow<SettingMainEvent>
    val isOnlyProfile: SharedFlow<Boolean>
    val notificationEnable : StateFlow<Boolean>
    val settingNotificationEvents : SharedFlow<SettingNotificationEvent>

    val settingUserEvents: SharedFlow<SettingUserEvent>
    val myInfo : StateFlow<MemberDetail>
    val modifyNameText: MutableStateFlow<String>
    val modifyTagText: MutableStateFlow<String>
    val isTagDuplication : MutableStateFlow<Boolean>

    fun back()
    fun goUser()
    fun goNotification()
    fun goNotice()
    fun goAgree()
    fun goInquire()
    fun goLicenses()
    fun goLogout()

    fun goDeleteAccount()

    fun singOutRequest()
    fun deleteAccountRequest()

    fun modifyMe()

    sealed class SettingMainEvent {
        object Back : SettingMainEvent()
        object GoUser : SettingMainEvent()
        object GoNotification : SettingMainEvent()
        object GoNotice : SettingMainEvent()
        object GoAgree : SettingMainEvent()
        object GoInquire : SettingMainEvent()
        object GoLicenses : SettingMainEvent()
        object GoLogout : SettingMainEvent()

        object GoAuthActivity: SettingMainEvent()
        object ShowLoading: SettingMainEvent()
        object DismissLoading: SettingMainEvent()

        object GoDeleteAccount : SettingMainEvent()
        data class ShowToastMessage(val message : String) : SettingMainEvent()
    }
    sealed class  SettingUserEvent {
        object  Back : SettingUserEvent()
        data class ShowToastMessage(val message : String) : SettingUserEvent()

    }

    sealed class  SettingNotificationEvent {
        object  Back : SettingNotificationEvent()
        data class ShowToastMessage(val message : String) : SettingNotificationEvent()

    }
}