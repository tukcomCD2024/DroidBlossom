package com.droidblossom.archive.presentation.ui.home.notification

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.member.NotificationModel
import com.droidblossom.archive.domain.usecase.member.GetNotificationsUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
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
class NotificationViewModelImpl @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : BaseViewModel(), NotificationViewModel {

    private val _notificationEvent = MutableSharedFlow<NotificationViewModel.NotificationEvent>()
    override val notificationEvent: SharedFlow<NotificationViewModel.NotificationEvent>
        get() = _notificationEvent.asSharedFlow()

    private val _notifications = MutableStateFlow(listOf<NotificationModel>())

    override val notifications: StateFlow<List<NotificationModel>>
        get() = _notifications

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage

    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    override fun getNotificationPage() {
        viewModelScope.launch {
            if (hasNextPage.value) {
                getNotificationsUseCase(15, lastCreatedTime.value).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _notifications.emit(notifications.value + it.notifications)
                        _lastCreatedTime.value = it.notifications.last().createdAt
                    }.onFail {
                        _notificationEvent.emit(
                            NotificationViewModel.NotificationEvent.ShowToastMessage(
                                "알림 불러오기 실패"
                            )
                        )
                    }
                }
            }
        }
    }
}