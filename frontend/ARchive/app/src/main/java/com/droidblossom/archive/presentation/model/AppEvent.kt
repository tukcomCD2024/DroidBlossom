package com.droidblossom.archive.presentation.model

sealed class AppEvent {
    object NetworkDisconnectedEvent : AppEvent()
    data class NotificationReceivedEvent(val message: String) : AppEvent()
}