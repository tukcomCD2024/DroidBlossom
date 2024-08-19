package com.droidblossom.archive.presentation.ui.home.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.member.NotificationModel
import com.droidblossom.archive.domain.usecase.member.GetNotificationsUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
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

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow =
        scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getNotificationListJob: Job? = null

    init {
        viewModelScope.launch {
            scrollEventFlow.collect {
                if (notifications.value.isEmpty()){
                    getLatestNotificationPage()
                }else{
                    getNotificationPage()
                }
            }
        }
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }

    override fun getNotificationPage() {
        if (hasNextPage.value) {
            getNotificationListJob?.cancel()
            getNotificationListJob = viewModelScope.launch {
                getNotificationsUseCase(
                    PagingRequestDto(
                        15,
                        _lastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _notifications.value = notifications.value + it.notifications
                        if (notifications.value.isNotEmpty()) {
                            _lastCreatedTime.value = it.notifications.last().createdAt
                        }
                    }.onFail {
                        _notificationEvent.emit(
                            NotificationViewModel.NotificationEvent.ShowToastMessage(
                                "알림 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."
                            )
                        )
                    }
                }
            }
        }

    }

    override fun getLatestNotificationPage() {
        getNotificationListJob?.cancel()
        getNotificationListJob = viewModelScope.launch {
            getNotificationsUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextPage.value = it.hasNext
                    _notifications.value = it.notifications
                    if (notifications.value.isNotEmpty()) {
                        _lastCreatedTime.value = it.notifications.last().createdAt
                    }
                }.onFail {
                    _notificationEvent.emit(
                        NotificationViewModel.NotificationEvent.ShowToastMessage(
                            "알림 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."
                        )
                    )
                }
            }
            _notificationEvent.emit(NotificationViewModel.NotificationEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

}