package com.droidblossom.archive.presentation.ui.splash

import kotlinx.coroutines.flow.SharedFlow

interface SplashViewModel {

    val splashEvents: SharedFlow<SplashEvent>

    fun splashEvent(event: SplashEvent)
    fun getServerCheck()

    sealed class SplashEvent {
        data class ShowToastMessage(val message : String) : SplashEvent()
        object Navigation : SplashEvent()

    }
}