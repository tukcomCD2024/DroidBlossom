package com.droidblossom.archive.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.R
import com.droidblossom.archive.domain.usecase.auth.ServerCheckUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val serverCheckUseCase: ServerCheckUseCase
) : BaseViewModel(), SplashViewModel {

    private val _splashEvents = MutableSharedFlow<SplashViewModel.SplashEvent>()
    override val splashEvents: SharedFlow<SplashViewModel.SplashEvent>
        get() = _splashEvents.asSharedFlow()

    override fun splashEvent(event: SplashViewModel.SplashEvent) {
        viewModelScope.launch {
            _splashEvents.emit(event)
        }
    }

    override fun getServerCheck() {
        viewModelScope.launch {
            delay(1000)
            serverCheckUseCase().collect { result ->
                result.onSuccess {
                    splashEvent(SplashViewModel.SplashEvent.Navigation)
                }.onFail {
                    splashEvent(
                        SplashViewModel.SplashEvent.ShowToastMessage(
                            "서버가 불안정합니다. " + ARchiveApplication.getString(R.string.reTryMessage)
                        )
                    )
                }
            }
        }
    }
}