package com.droidblossom.archive.presentation.ui.home.dialog

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleSummaryUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CapsulePreviewDialogViewModelImpl @Inject constructor(
    private val secretCapsuleSummaryUseCase: SecretCapsuleSummaryUseCase
) : BaseViewModel(), CapsulePreviewDialogViewModel {

    private val _secretCapsuleSummary = MutableStateFlow(SecretCapsuleSummary("","","","","","","", false, ""))
    override val secretCapsuleSummary: StateFlow<SecretCapsuleSummary>
        get() = _secretCapsuleSummary

    private val _startTime = MutableStateFlow<Calendar?>(null)
    private val _endTime = MutableStateFlow<Calendar?>(null)

    override val startTime: StateFlow<Calendar?> = _startTime
    override val endTime: StateFlow<Calendar?> = _endTime

    private val _totalTime = MutableStateFlow<Int?>(0)
    override val totalTime: StateFlow<Int?> = _totalTime.asStateFlow()

    private val _initialProgress = MutableStateFlow<Int?>(0)
    override val initialProgress: StateFlow<Int?> = _initialProgress.asStateFlow()

    private val _timerState = MutableStateFlow("00:00")
    override val timerState: StateFlow<String> = _timerState.asStateFlow()

    private val _visibleCapsuleOpenMessage = MutableStateFlow(true)
    override val visibleCapsuleOpenMessage: StateFlow<Boolean> = _visibleCapsuleOpenMessage.asStateFlow()

    private val _visibleTimeProgressBar = MutableStateFlow(false)
    override val visibleTimeProgressBar: StateFlow<Boolean> = _visibleTimeProgressBar.asStateFlow()

    private val _visibleOpenProgressBar = MutableStateFlow(false)
    override val visibleOpenProgressBar: StateFlow<Boolean> = _visibleOpenProgressBar.asStateFlow()

    private val _capsuleTypeImage = MutableStateFlow(0)
    override val capsuleTypeImage: StateFlow<Int> = _capsuleTypeImage.asStateFlow()

    fun getSecretCapsuleSummary(capsuleId: Int) {
        viewModelScope.launch {
            secretCapsuleSummaryUseCase(capsuleId).collect { result ->
                result.onSuccess {
                    _secretCapsuleSummary.emit(it)
                    if (!secretCapsuleSummary.value.isOpened){
                        calculateCapsuleOpenTime(it.createdAt, it.dueDate)
                    }
                }.onFail {

                }.onException {

                }.onError {

                }
            }
        }
    }

    override fun calculateCapsuleOpenTime(createdAt: String, dueDate: String) {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

            val startTimeCalendar = Calendar.getInstance().apply {
                time = dateFormat.parse(createdAt) ?: throw IllegalArgumentException("Invalid createdAt format")
            }

            val endTimeCalendar = Calendar.getInstance().apply {
                time = dateFormat.parse(dueDate) ?: throw IllegalArgumentException("Invalid dueDate format")
            }
            _startTime.emit(startTimeCalendar)
            _endTime.emit(endTimeCalendar)
        }
    }

    override fun setProgressBar() {
        viewModelScope.launch {
            val endTimeMillis = endTime.value?.timeInMillis ?: return@launch
            val startTimeMillis = startTime.value?.timeInMillis ?: return@launch
            val currentTimeMillis = System.currentTimeMillis()

            if (currentTimeMillis > endTimeMillis) {
                _visibleCapsuleOpenMessage.emit(true)
                _visibleTimeProgressBar.emit(false)
                _visibleOpenProgressBar.emit(true)
            } else {
                val totalTimeLong = endTimeMillis - startTimeMillis
                val initialProgressLong = currentTimeMillis - startTimeMillis

                val totalTimeInt = if (totalTimeLong > Int.MAX_VALUE) Int.MAX_VALUE else totalTimeLong.toInt()
                val initialProgressInt = if (initialProgressLong > Int.MAX_VALUE) Int.MAX_VALUE else initialProgressLong.toInt()

                _totalTime.emit(totalTimeInt)
                _initialProgress.emit(initialProgressInt)

                _visibleCapsuleOpenMessage.emit(false)
                _visibleTimeProgressBar.emit(true)
                _visibleOpenProgressBar.emit(false)
                updateTimerState(endTimeMillis)
            }
        }
    }

    private fun updateTimerState(endTimeMillis: Long) {
        viewModelScope.launch {
            val remainingTime = endTimeMillis - System.currentTimeMillis()

            if (remainingTime > 24 * 60 * 60 * 1000) {
                _timerState.emit(formatReleaseDate(Calendar.getInstance().apply { timeInMillis = endTimeMillis }))
                delay(60000)
            } else {
                startTimer(endTimeMillis)
            }
        }
    }

    private fun startTimer(endTimeMillis: Long) {
        viewModelScope.launch {
            var remainingTime = endTimeMillis - System.currentTimeMillis()

            while (remainingTime > 0) {
                _timerState.emit(getTime(remainingTime))
                delay(60000)
                remainingTime = endTimeMillis - System.currentTimeMillis()
            }

            _timerState.emit("00:00")
            _visibleCapsuleOpenMessage.emit(true)
            _visibleTimeProgressBar.emit(false)
            _visibleOpenProgressBar.emit(true)
        }
    }
    private fun getTime(millis: Long): String {
        val hours = (millis / (1000 * 60 * 60)) % 24
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d:%02d", hours, minutes)
    }

    private fun formatReleaseDate(calendar: Calendar?): String {
        if (calendar == null) return ""
        val dateFormat = SimpleDateFormat("yy MMM dd", Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    }

    override fun setCapsuleTypeImage(image : Int){
        viewModelScope.launch {
            _capsuleTypeImage.emit(image)
        }
    }

}
