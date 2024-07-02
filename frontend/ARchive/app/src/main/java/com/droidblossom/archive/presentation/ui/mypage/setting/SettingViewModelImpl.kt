package com.droidblossom.archive.presentation.ui.mypage.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.usecase.member.NotificationEnabledUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModel
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModel
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private val dataStoreUtils: DataStoreUtils
) : BaseViewModel(), SettingViewModel {

    //main
    private val _settingMainEvents = MutableSharedFlow<SettingViewModel.SettingMainEvent>()
    override val settingMainEvents: SharedFlow<SettingViewModel.SettingMainEvent>
        get() = _settingMainEvents.asSharedFlow()
    private val _isOnlyProfile = MutableStateFlow(false)
    override val isOnlyProfile :StateFlow<Boolean>
        get() = _isOnlyProfile

    //notification
    private val _notificationEnable = MutableStateFlow<Boolean>(false)
    override val notificationEnable: StateFlow<Boolean>
        get() = _notificationEnable

    private val _settingNotificationEvent =
        MutableSharedFlow<SettingViewModel.SettingNotificationEvent>()
    override val settingNotificationEvents: SharedFlow<SettingViewModel.SettingNotificationEvent>
        get() = _settingNotificationEvent.asSharedFlow()


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
                        Log.d("알림변경", "성공")
                        _settingNotificationEvent.emit(
                            SettingViewModel.SettingNotificationEvent.ShowToastMessage(
                                "알림 설정을 변경했습니다."
                            )
                        )
                    }.onFail {
                        Log.d("알림변경", "실패")
                        _settingNotificationEvent.emit(
                            SettingViewModel.SettingNotificationEvent.ShowToastMessage(
                                "알림을 변경 실패."
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
}