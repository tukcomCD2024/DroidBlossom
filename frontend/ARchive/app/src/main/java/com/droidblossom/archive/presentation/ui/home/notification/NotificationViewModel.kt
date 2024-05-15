package com.droidblossom.archive.presentation.ui.home.notification

import com.droidblossom.archive.domain.model.member.NotificationModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface NotificationViewModel {
    val notificationEvent: SharedFlow<NotificationEvent>

    val notifications: StateFlow<List<NotificationModel>>
    val hasNextPage: StateFlow<Boolean>
    val lastCreatedTime: StateFlow<String>

    fun getNotificationPage()
    fun getLastNotificationPage()
    fun onScrollNearBottom()

    sealed class NotificationEvent {
        data class ShowToastMessage(val message: String) : NotificationEvent()
    }
}