package com.droidblossom.archive.presentation.ui

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MainViewModel {

    val mainEvents: SharedFlow<MainEvent>
    val selectedMainTab: StateFlow<MainActivity.MainPage?>

    fun setMainTab(selectedTab : MainActivity.MainPage)
    fun mainEvent(event: MainEvent)

    sealed class MainEvent {
        object NavigateToCamera : MainEvent()
        object NavigateToHome : MainEvent()
        object NavigateToMyPage : MainEvent()
        object NavigateToSocial : MainEvent()
        object NavigateToSkin : MainEvent()
        data class ShowToastMessage(val message: String) : MainEvent()
    }
}