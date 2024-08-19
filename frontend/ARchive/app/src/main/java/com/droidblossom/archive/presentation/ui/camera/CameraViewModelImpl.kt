package com.droidblossom.archive.presentation.ui.camera

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.ARchiveApplication.Companion.getString
import com.droidblossom.archive.R
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
class CameraViewModelImpl @Inject constructor(
    private val nearbyMyCapsulesARUseCase: NearbyMyCapsulesARUseCase,
    private val nearbyFriendsCapsulesARUseCase: NearbyFriendsCapsulesARUseCase
) : BaseViewModel(), CameraViewModel {

    private val _cameraEvents = MutableSharedFlow<CameraViewModel.CameraEvent>()
    override val cameraEvents: SharedFlow<CameraViewModel.CameraEvent>
        get() = _cameraEvents.asSharedFlow()

    private val _capsuleList = MutableStateFlow(listOf<CapsuleAnchor>())
    override val capsuleList: StateFlow<List<CapsuleAnchor>>
        get() = _capsuleList

    override var capsuleListSize = -1

    private val _anchorNodes = MutableStateFlow<MutableList<AnchorNode>>(mutableListOf())
    override val anchorNodes get() = _anchorNodes

    private val _isFriendsCapsuleDisplay = MutableStateFlow(false)
    override val isFriendsCapsuleDisplay: StateFlow<Boolean>
        get() = _isFriendsCapsuleDisplay

    private val _selectedCapsuleFilter =
        MutableStateFlow(CameraViewModel.CapsuleFilterType.FILTER_ALL)
    override val selectedCapsuleFilter: StateFlow<CameraViewModel.CapsuleFilterType>
        get() = _selectedCapsuleFilter
    override var isCapsulesAdded = false


    override fun addAnchorNode(anchorNode: AnchorNode) {
        val updatedList = _anchorNodes.value.toMutableList()
        updatedList.add(anchorNode)
        _anchorNodes.value = updatedList
    }

    override fun clearAnchorNode() {
        isCapsulesAdded = false
        capsuleListSize = -1
        _capsuleList.value = mutableListOf()
        _anchorNodes.value = mutableListOf()
        capsuleListSize = 0
    }


    override fun cameraEvent(event: CameraViewModel.CameraEvent) {
        viewModelScope.launch {
            _cameraEvents.emit(event)
        }
    }

    override fun getCapsules(latitude: Double, longitude: Double) {
        when (selectedCapsuleFilter.value) {
            CameraViewModel.CapsuleFilterType.FILTER_ALL -> {
                getMyCapsules(latitude, longitude)
            }

            CameraViewModel.CapsuleFilterType.FILTER_SECRET -> {
                getMyCapsules(latitude, longitude)
            }

            CameraViewModel.CapsuleFilterType.FILTER_GROUP -> {
                getMyCapsules(latitude, longitude)
            }

            CameraViewModel.CapsuleFilterType.FILTER_PUBLIC_MY -> {
                getMyCapsules(latitude, longitude)
            }

            CameraViewModel.CapsuleFilterType.FILTER_PUBLIC_FRIEND -> {
                getFriendsCapsules(latitude, longitude)
            }

            CameraViewModel.CapsuleFilterType.FILTER_TREASURE -> {
                getMyCapsules(latitude, longitude)
            }
        }

    }

    override fun selectFilter(
        capsuleFilterType: CameraViewModel.CapsuleFilterType,
        latitude: Double,
        longitude: Double
    ) {
        _selectedCapsuleFilter.value = capsuleFilterType
        getCapsules(latitude, longitude)
    }


    private fun getMyCapsules(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            nearbyMyCapsulesARUseCase(
                latitude,
                longitude,
                0.1,
                selectedCapsuleFilter.value.description
            ).collect { result ->
                result.onSuccess {
                    capsuleListSize = it.capsuleAnchors.size
                    _capsuleList.value = it.capsuleAnchors
                    if (capsuleList.value.isEmpty()) {
                        cameraEvent(CameraViewModel.CameraEvent.ShowToastMessage("주변에 캡슐이 없습니다."))
                        cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                        capsuleListSize = 0
                    }
                }.onFail {
                    if (it == 400) {
                        cameraEvent(CameraViewModel.CameraEvent.ShowToastMessage("현재 위치가 대한민국 내에 있는지 확인해주세요. 대한민국 내에서만 주변 캡슐을 조회할 수 있습니다."))
                    } else {
                        cameraEvent(
                            CameraViewModel.CameraEvent.ShowToastMessage(
                                "캡슐을 불러오는데 실패했습니다. " + getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                    cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                }
            }
        }
    }

    private fun getFriendsCapsules(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            nearbyFriendsCapsulesARUseCase(latitude, longitude, 0.1).collect { result ->
                result.onSuccess {
                    capsuleListSize = it.capsuleAnchors.size
                    _capsuleList.value = it.capsuleAnchors
                    if (capsuleList.value.isEmpty()) {
                        cameraEvent(CameraViewModel.CameraEvent.ShowToastMessage("주변에 캡슐이 없습니다."))
                        cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                        capsuleListSize = 0
                    }
                }.onFail {
                    if (it == 400) {
                        cameraEvent(CameraViewModel.CameraEvent.ShowToastMessage("현재 위치가 대한민국 내에 있는지 확인해주세요. 대한민국 내에서만 주변 캡슐을 조회할 수 있습니다."))
                    } else {
                        cameraEvent(
                            CameraViewModel.CameraEvent.ShowToastMessage(
                                "캡슐을 불러오는데 실패했습니다. " + ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                    cameraEvent(CameraViewModel.CameraEvent.DismissLoading)
                }
            }
        }
    }

}