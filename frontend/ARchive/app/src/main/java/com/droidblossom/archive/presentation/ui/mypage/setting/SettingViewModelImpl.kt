package com.droidblossom.archive.presentation.ui.mypage.setting

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModelImpl @Inject constructor(

) : BaseViewModel(), SettingViewModel {

    //main
    private val _settingMainEvents = MutableSharedFlow<SettingViewModel.SettingMainEvent>()
    override val settingMainEvents: SharedFlow<SettingViewModel.SettingMainEvent>
        get() = _settingMainEvents.asSharedFlow()

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
}