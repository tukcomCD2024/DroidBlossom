package com.droidblossom.archive.presentation.ui.camera

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchor
import com.droidblossom.archive.domain.usecase.capsule.NearbyFriendsCapsulesARUseCase
import com.droidblossom.archive.domain.usecase.capsule.NearbyMyCapsulesARUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.ar.node.AnchorNode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModelImpl@Inject constructor(
    private val nearbyMyCapsulesARUseCase: NearbyMyCapsulesARUseCase,
    private val nearbyFriendsCapsulesARUseCase: NearbyFriendsCapsulesARUseCase
) : BaseViewModel(), CameraViewModel {

    private val _cameraEvents = MutableSharedFlow<CameraViewModel.CameraEvent>()
    override val cameraEvents: SharedFlow<CameraViewModel.CameraEvent>
        get() = _cameraEvents.asSharedFlow()

    private val _capsuleList = MutableStateFlow(listOf<CapsuleAnchor>())
    override val capsuleList: StateFlow<List<CapsuleAnchor>>
        get() = _capsuleList

    private val _capsuleListSize = MutableStateFlow(-1)
    override val capsuleListSize = _capsuleListSize.asStateFlow()

    private val _anchorNodes = MutableStateFlow<MutableList<AnchorNode>>(mutableListOf())
    override val anchorNodes get() =  _anchorNodes

    private val _isFriendsCapsuleDisplay = MutableStateFlow(false)
    override val isFriendsCapsuleDisplay: StateFlow<Boolean>
        get() = _isFriendsCapsuleDisplay


    override fun addAnchorNode(anchorNode: AnchorNode) {
        val updatedList = _anchorNodes.value.toMutableList()
        updatedList.add(anchorNode)
        _anchorNodes.value = updatedList
    }

    override fun clearAnchorNode() {
        _anchorNodes.value = mutableListOf()
        _capsuleListSize.value = 0
    }


    override fun cameraEvent(event: CameraViewModel.CameraEvent) {
        viewModelScope.launch {
            _cameraEvents.emit(event)
        }
    }

    override fun getCapsules(latitude: Double, longitude: Double){
        if (!isFriendsCapsuleDisplay.value) getMyCapsules(latitude, longitude)
        else getFriendsCapsules(latitude, longitude)
    }

    private fun getMyCapsules(latitude: Double, longitude: Double){
        viewModelScope.launch {
            nearbyMyCapsulesARUseCase(latitude,longitude,0.1,"ALL").collect{ result->
                result.onSuccess {
                    _capsuleList.emit(it.capsuleAnchors)
                    _capsuleListSize.value = _capsuleList.value.size
                    if (capsuleList.value.isEmpty()){
                        cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                    }
                }.onFail {
                    cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                }
            }
        }
    }
    private fun getFriendsCapsules(latitude: Double, longitude: Double){
        viewModelScope.launch {
            nearbyFriendsCapsulesARUseCase(latitude,longitude,0.1).collect{ result->
                result.onSuccess {
                    _capsuleList.emit(it.capsuleAnchors)
                    _capsuleListSize.value = _capsuleList.value.size
                    if (capsuleList.value.isEmpty()) {
                        cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                        _capsuleList.emit(listOf())
                    }
                }.onFail {
                    cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                }
            }
        }
    }

    override fun clickFriendsDisplay() {
        viewModelScope.launch { _isFriendsCapsuleDisplay.emit(!isFriendsCapsuleDisplay.value) }
    }
}