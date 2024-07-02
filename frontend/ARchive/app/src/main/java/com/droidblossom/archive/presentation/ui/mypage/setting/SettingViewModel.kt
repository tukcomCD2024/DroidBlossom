package com.droidblossom.archive.presentation.ui.mypage.setting

import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SettingViewModel {

    val settingMainEvents: SharedFlow<SettingMainEvent>
    val isOnlyProfile: SharedFlow<Boolean>
    val notificationEnable : StateFlow<Boolean>
    val settingNotificationEvents : SharedFlow<SettingNotificationEvent>

    fun back()
    fun goUser()
    fun goNotification()
    fun goNotice()
    fun goAgree()
    fun goInquire()
    fun goLicenses()
    fun goLogout()

    sealed class SettingMainEvent {
        object Back : SettingMainEvent()
        object GoUser : SettingMainEvent()
        object GoNotification : SettingMainEvent()
        object GoNotice : SettingMainEvent()
        object GoAgree : SettingMainEvent()
        object GoInquire : SettingMainEvent()
        object GoLicenses : SettingMainEvent()
        object GoLogout : SettingMainEvent()
        data class ShowToastMessage(val message : String) : SettingMainEvent()
    }

    sealed class  SettingNotificationEvent {
        object  Back : SettingNotificationEvent()
        data class ShowToastMessage(val message : String) : SettingNotificationEvent()
    }
}