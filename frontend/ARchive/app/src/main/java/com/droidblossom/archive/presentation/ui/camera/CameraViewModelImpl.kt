package com.droidblossom.archive.presentation.ui.camera

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.droidblossom.archive.domain.usecase.capsule.NearbyCapsulesUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModelImpl@Inject constructor(
    private val nearbyCapsulesUseCase: NearbyCapsulesUseCase
) : BaseViewModel(), CameraViewModel {

    private val _cameraEvents = MutableSharedFlow<CameraViewModel.CameraEvent>()
    override val cameraEvents: SharedFlow<CameraViewModel.CameraEvent>
        get() = _cameraEvents.asSharedFlow()

    private val _capsuleList = MutableStateFlow(listOf<CapsuleMarker>())
    override val capsuleList: StateFlow<List<CapsuleMarker>>
        get() = _capsuleList

    override fun cameraEvent(event: CameraViewModel.CameraEvent) {
        viewModelScope.launch {
            _cameraEvents.emit(event)
        }
    }
    override fun getCapsules(latitude: Double, longitude: Double) : List<CapsuleMarker> {
        viewModelScope.launch {
            nearbyCapsulesUseCase(latitude,longitude,1.0,"ALL").collect{ result->
                result.onSuccess {
                    _capsuleList.emit(it.capsules)
                }.onFail {

                }
            }
        }
        return capsuleList.value
    }
}