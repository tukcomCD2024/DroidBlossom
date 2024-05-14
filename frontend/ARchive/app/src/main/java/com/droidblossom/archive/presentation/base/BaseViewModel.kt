package com.droidblossom.archive.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

abstract class BaseViewModel:ViewModel() {

    open fun fetchData(): Job = viewModelScope.launch {  }

    companion object {
        fun <T> Flow<T>.throttleFirst(windowDuration: Long, timeUnit: TimeUnit): Flow<T> = channelFlow {
            val windowMillis = timeUnit.toMillis(windowDuration)
            var lastTime = 0L
            collect { value ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime >= windowMillis) {
                    lastTime = currentTime
                    send(value)
                }
            }
        }
    }
}