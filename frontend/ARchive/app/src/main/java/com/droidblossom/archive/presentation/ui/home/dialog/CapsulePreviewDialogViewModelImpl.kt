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
    
    private fun getTime(millis: Long): String {
        val hours = (millis / (1000 * 60 * 60)) % 24
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d시간 %02d분", hours, minutes)
    }

}
