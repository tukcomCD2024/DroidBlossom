package com.droidblossom.archive.presentation.model

sealed class AppEvent {
    object NetworkDisconnectedEvent : AppEvent()
    object AppKeyErrorEvent : AppEvent()
    object BadGateEvent: AppEvent()
    data class NotificationReceivedEvent(val message: String) : AppEvent()
}