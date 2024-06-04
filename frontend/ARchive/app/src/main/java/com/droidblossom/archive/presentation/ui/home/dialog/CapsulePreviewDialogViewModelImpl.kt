package com.droidblossom.archive.presentation.ui.home.dialog

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember
import com.droidblossom.archive.domain.usecase.capsule.PatchCapsuleOpenedUseCase
import com.droidblossom.archive.domain.usecase.group_capsule.GroupCapsuleOpenUseCase
import com.droidblossom.archive.domain.usecase.group_capsule.GroupCapsuleSummaryUseCase
import com.droidblossom.archive.domain.usecase.open.PublicCapsuleSummaryUseCase
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleSummaryUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Long.min
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CapsulePreviewDialogViewModelImpl @Inject constructor(
    private val secretCapsuleSummaryUseCase: SecretCapsuleSummaryUseCase,
    private val publicCapsuleSummaryUseCase: PublicCapsuleSummaryUseCase,
    private val patchCapsuleOpenedUseCase: PatchCapsuleOpenedUseCase,
    private val groupCapsuleOpenUseCase: GroupCapsuleOpenUseCase,
    private val groupCapsuleSummaryUseCase: GroupCapsuleSummaryUseCase
) : BaseViewModel(), CapsulePreviewDialogViewModel {

    private val _capsulePreviewDialogEvents = MutableSharedFlow<CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent>()
    override val capsulePreviewDialogEvents: SharedFlow<CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent> = _capsulePreviewDialogEvents.asSharedFlow()

    private val _capsuleSummaryResponse = MutableStateFlow(CapsuleSummaryResponse("","","","","","","", false, ""))
    override val capsuleSummaryResponse: StateFlow<CapsuleSummaryResponse>
        get() = _capsuleSummaryResponse

    private val _startTime = MutableStateFlow<Calendar?>(null)
    private val _endTime = MutableStateFlow<Calendar?>(null)

    override val startTime: StateFlow<Calendar?> = _startTime
    override val endTime: StateFlow<Calendar?> = _endTime

    private val _totalTime = MutableStateFlow<Int?>(0)
    override val totalTime: StateFlow<Int?> = _totalTime.asStateFlow()

    private val _timeProgress = MutableStateFlow<Int?>(0)
    override val timeProgress: StateFlow<Int?> = _timeProgress.asStateFlow()

    private val _timerState = MutableStateFlow("00:00")
    override val timerState: StateFlow<String> = _timerState.asStateFlow()

    private val _capsuleOpenState = MutableStateFlow(false)
    override val capsuleOpenState: StateFlow<Boolean> = _capsuleOpenState.asStateFlow()

    private val _visibleCapsuleOpenMessage = MutableStateFlow(true)
    override val visibleCapsuleOpenMessage: StateFlow<Boolean> = _visibleCapsuleOpenMessage.asStateFlow()

    private val _visibleTimeProgressBar = MutableStateFlow(false)
    override val visibleTimeProgressBar: StateFlow<Boolean> = _visibleTimeProgressBar.asStateFlow()

    private val _visibleOpenProgressBar = MutableStateFlow(false)
    override val visibleOpenProgressBar: StateFlow<Boolean> = _visibleOpenProgressBar.asStateFlow()

    private val _capsuleTypeImage = MutableStateFlow(0)
    override val capsuleTypeImage: StateFlow<Int> = _capsuleTypeImage.asStateFlow()

    private val _calledFromCamera = MutableStateFlow(false)
    override val calledFromCamera: StateFlow<Boolean>
        get() = _calledFromCamera

    private val _timeCapsule = MutableStateFlow(false)
    override val timeCapsule: StateFlow<Boolean> get() = _timeCapsule

    private val _capsuleType = MutableStateFlow(HomeFragment.CapsuleType.GROUP)
    override val capsuleType: StateFlow<HomeFragment.CapsuleType> get() = _capsuleType

    private val _groupCapsuleMembers = MutableStateFlow(emptyList<GroupCapsuleMember>())
    override val groupCapsuleMembers: StateFlow<List<GroupCapsuleMember>> get() = _groupCapsuleMembers


    override fun capsulePreviewDialogEvent(event: CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent){
        viewModelScope.launch {
            _capsulePreviewDialogEvents.emit(event)
        }
    }

    override fun setCalledFromCamera(calledFromCamera : Boolean){
        _calledFromCamera.value = calledFromCamera
    }
    override fun getSecretCapsuleSummary(capsuleId: Long) {
        viewModelScope.launch {
            secretCapsuleSummaryUseCase(capsuleId).collect { result ->
                result.onSuccess {
                    _capsuleSummaryResponse.emit(it)
                    _capsuleOpenState.emit(capsuleSummaryResponse.value.isOpened)
                    if (!capsuleOpenState.value){
                        calculateCapsuleOpenTime(it.createdAt, it.dueDate)
                    }
                }.onFail {

                }
            }
        }
    }

    override fun getPublicCapsuleSummary(capsuleId: Long) {
        viewModelScope.launch {
            publicCapsuleSummaryUseCase(capsuleId).collect { result ->
                result.onSuccess {
                    _capsuleSummaryResponse.emit(it)
                    _capsuleOpenState.emit(capsuleSummaryResponse.value.isOpened)
                    if (!capsuleOpenState.value){
                        calculateCapsuleOpenTime(it.createdAt, it.dueDate)
                    }
                }.onFail {

                }
            }
        }
    }

    override fun getGroupCapsuleSummary(capsuleId: Long) {
        viewModelScope.launch {
            groupCapsuleSummaryUseCase(capsuleId).collect { result ->
                result.onSuccess {
                    _capsuleSummaryResponse.emit(it.toCapsuleSummaryResponseModel())
                    _groupCapsuleMembers.emit(it.members)
                    _capsuleOpenState.emit(capsuleSummaryResponse.value.isOpened)
                    if (!capsuleOpenState.value){
                        calculateCapsuleOpenTime(it.createdAt, it.dueDate)
                    }
                }.onFail {

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
                if (dueDate.isBlank()) {
                    time = startTimeCalendar.time
                }else{
                    time = dateFormat.parse(dueDate) ?: throw IllegalArgumentException("Invalid createdAt format")
                }
            }
            if (startTimeCalendar == endTimeCalendar) _timeCapsule.emit(false) else _timeCapsule.emit(true)
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
                val totalTimeSeconds = (endTimeMillis - startTimeMillis)
                val initialProgressSeconds = (currentTimeMillis - startTimeMillis)

                val totalTimeInt = if (totalTimeSeconds > Int.MAX_VALUE) Int.MAX_VALUE else totalTimeSeconds.toInt()
                val initialProgressInt = if (initialProgressSeconds > Int.MAX_VALUE) Int.MAX_VALUE else initialProgressSeconds.toInt()

                _totalTime.emit(totalTimeInt)
                _timeProgress.emit(initialProgressInt)

                _visibleCapsuleOpenMessage.emit(false)
                _visibleTimeProgressBar.emit(true)
                _visibleOpenProgressBar.emit(false)
                startTimer(startTimeMillis, endTimeMillis)
            }
        }
    }

    private fun startTimer(startTimeMillis: Long, endTimeMillis: Long) {
        viewModelScope.launch {
            var currentTimeMillis = System.currentTimeMillis()
            var remainingTime = endTimeMillis - currentTimeMillis

            while (remainingTime > 0) {
                val elapsed = currentTimeMillis - startTimeMillis
                val initialProgress = min(elapsed, endTimeMillis - startTimeMillis).toInt()

                _timeProgress.emit(initialProgress)

                if (remainingTime > 24 * 60 * 60 * 1000) {
                    _timerState.emit(formatReleaseDate(Calendar.getInstance().apply { timeInMillis = endTimeMillis }))
                }else{
                    _timerState.emit(getTime(remainingTime))
                }

                delay(1000)
                currentTimeMillis = System.currentTimeMillis()
                remainingTime = endTimeMillis - currentTimeMillis
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

    override fun setCapsuleTypeImage(image : Int, type: HomeFragment.CapsuleType){
        viewModelScope.launch {
            _capsuleTypeImage.emit(image)
            _capsuleType.emit(type)
        }
    }

    override fun openCapsule(capsuleId: Long) {
        if (timeCapsule.value && !calledFromCamera.value) {
            capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowToastMessage("타임캡슐 첫 오픈 시에는 시간, 위치 제약 있습니다."))
            return
        }

        viewModelScope.launch {
            Log.d("흠", "${capsuleType.value}")
            when(capsuleType.value){
                HomeFragment.CapsuleType.GROUP -> {
                    groupCapsuleOpenUseCase(capsuleId).collect { result ->
                        result.onSuccess {
                            // 값 뭐뭐 받는지에 따라서 다름
                        }.onFail {
                            // 값 뭐뭐 받는지에 따라서 다름
                        }
                    }
                }

                else -> {
                    patchCapsuleOpenedUseCase(capsuleId).collect { result ->
                        result.onSuccess {
                            if (it.result == "캡슐을 열 수 없습니다.") {
                                capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowToastMessage(it.result))
                            } else {
                                _capsuleOpenState.emit(true)
                                capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.CapsuleOpenSuccess)
                            }
                        }.onFail {
                            Log.d("개봉", " 개봉 실패 코드 : $it")
                            capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowToastMessage("캡슐 열기 실패"))
                        }
                    }
                }
            }
        }
    }

    override fun setVisibleOpenProgressBar(visible: Boolean) {
        _visibleOpenProgressBar.value = visible
    }

}
