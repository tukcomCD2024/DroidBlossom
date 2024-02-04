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

    private val _secretCapsuleSummary = MutableStateFlow(SecretCapsuleSummary("","","","","",false,""))
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

    private val _timerState = MutableStateFlow("0일 00시간 00분")
    override val timerState: StateFlow<String> = _timerState.asStateFlow()

    fun getSecretCapsuleSummary(capsuleId: Int) {
        viewModelScope.launch {
            secretCapsuleSummaryUseCase(capsuleId).collect { result ->
                result.onSuccess {
                    _secretCapsuleSummary.emit(it)
                    calculateCapsuleOpenTime(it.createdAt, it.dueDate)
                    Log.d("getSecretCapsuleSummary", "$it")
                }.onFail {

                }.onException {

                }.onError {

                }
            }
        }
    }

    override fun calculateCapsuleOpenTime(createdAt: String, dueDate: String) {
        if (dueDate.isEmpty()) return
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
            val totalTimeLong = endTimeMillis - startTimeMillis
            val currentTimeMillis = System.currentTimeMillis()
            val initialProgressLong = currentTimeMillis - startTimeMillis

            val totalTimeInt = if (totalTimeLong > Int.MAX_VALUE) Int.MAX_VALUE else totalTimeLong.toInt()
            val initialProgressInt = if (initialProgressLong > Int.MAX_VALUE) Int.MAX_VALUE else initialProgressLong.toInt()

            _totalTime.emit(totalTimeInt)
            _initialProgress.emit(initialProgressInt)

            if (totalTimeLong > 24 * 60 * 60 * 1000) {
                _timerState.emit(formatReleaseDate(endTime.value))
            } else {
                startTimer()
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            val endTimeMillis = endTime.value?.timeInMillis ?: return@launch
            var remainingTime = endTimeMillis - System.currentTimeMillis()

            while (remainingTime > 0) {
                _timerState.emit(getTime(remainingTime))
                delay(60000)
                remainingTime = endTimeMillis - System.currentTimeMillis()
            }

            _timerState.emit("00시간 00분")
        }
    }
    private fun getTime(millis: Long): String {
        val hours = (millis / (1000 * 60 * 60)) % 24
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d시간 %02d분", hours, minutes)
    }

    private fun formatReleaseDate(calendar: Calendar?): String {
        if (calendar == null) return ""
        val dateFormat = SimpleDateFormat("yy MMM dd", Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    }

}
