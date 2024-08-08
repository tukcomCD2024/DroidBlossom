package com.droidblossom.archive.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.usecase.auth.ServerCheckUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val serverCheckUseCase: ServerCheckUseCase
): BaseViewModel(), SplashViewModel {

    private val _splashEvents = MutableSharedFlow<SplashViewModel.SplashEvent>()
    override val splashEvents: SharedFlow<SplashViewModel.SplashEvent>
        get() = _splashEvents.asSharedFlow()

    override fun splashEvent(event: SplashViewModel.SplashEvent) {
        viewModelScope.launch {
            _splashEvents.emit(event)
        }
    }

    override fun getServerCheck() {
        viewModelScope.launch{
            serverCheckUseCase().collect{ result ->
                result.onSuccess {
                    splashEvent(SplashViewModel.SplashEvent.Navigation)
                }.onFail {

                }
            }
        }
    }

}