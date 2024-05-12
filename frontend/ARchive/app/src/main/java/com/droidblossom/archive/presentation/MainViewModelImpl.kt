package com.droidblossom.archive.presentation

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.MainViewModel
import com.droidblossom.archive.util.DataStoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val dataStoreUtils: DataStoreUtils
): BaseViewModel(), MainViewModel {


    private val _mainEvents = MutableSharedFlow<MainViewModel.MainEvent>()
    override val mainEvents: SharedFlow<MainViewModel.MainEvent>
        get() = _mainEvents.asSharedFlow()

    private val _selectedMainTab = MutableStateFlow<MainActivity.MainPage?>(null)
    override val selectedMainTab: StateFlow<MainActivity.MainPage?>
        get() = _selectedMainTab

    init {
        viewModelScope.launch {
            val selectedTabId = dataStoreUtils.fetchSelectedTab()
            val selectedTab = MainActivity.MainPage.values().find { it.name == selectedTabId }
            _selectedMainTab.value = selectedTab ?: MainActivity.MainPage.HOME
        }
    }

    override fun setMainTab(selectedTab : MainActivity.MainPage) {
        _selectedMainTab.value = selectedTab
    }

    override fun mainEvent(event: MainViewModel.MainEvent) {
        viewModelScope.launch {
            _mainEvents.emit(event)
        }
    }


}