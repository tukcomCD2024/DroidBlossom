package com.droidblossom.archive.presentation.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.capsule.CapsuleMarker
import com.droidblossom.archive.domain.usecase.capsule.NearbyFriendsCapsulesHomeUseCase
import com.droidblossom.archive.domain.usecase.capsule.NearbyMyCapsulesHomeUseCase
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
class HomeViewModelImpl @Inject constructor(
    private val nearbyMyCapsulesHomeUseCase: NearbyMyCapsulesHomeUseCase,
    private val nearbyFriendsCapsulesHomeUseCase: NearbyFriendsCapsulesHomeUseCase,
) : BaseViewModel(), HomeViewModel {

    private val _homeEvents = MutableSharedFlow<HomeViewModel.HomeEvent>()
    override val homeEvents: SharedFlow<HomeViewModel.HomeEvent>
        get() = _homeEvents.asSharedFlow()

    private val _filterCapsuleSelect: MutableStateFlow<HomeViewModel.CapsuleFilter> =
        MutableStateFlow(HomeViewModel.CapsuleFilter.ALL)
    override val filterCapsuleSelect: StateFlow<HomeViewModel.CapsuleFilter>
        get() = _filterCapsuleSelect

    private val _isClickedFAB: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isClickedFAB: StateFlow<Boolean>
        get() = _isClickedFAB
    private val _existsNotification: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val existsNotification: StateFlow<Boolean>
        get() = _existsNotification
    private val _followLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val followLocation: StateFlow<Boolean>
        get() = _followLocation

    private val _isFriendsCapsuleDisplay = MutableStateFlow(false)
    override val isFriendsCapsuleDisplay: StateFlow<Boolean>
        get() = _isFriendsCapsuleDisplay
    private val _capsuleList = MutableStateFlow<List<CapsuleMarker>>(listOf())
    override val capsuleList: StateFlow<List<CapsuleMarker>>
        get() = _capsuleList

    override fun homeEvent(event: HomeViewModel.HomeEvent) {
        viewModelScope.launch {
            _homeEvents.emit(event)
        }
    }

    override fun selectPublic() {
        viewModelScope.launch {

            if (filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.PUBLIC)
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.ALL)
            else
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.PUBLIC)
        }
    }

    override fun selectGroup() {
        viewModelScope.launch {
            if (filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.GROUP)
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.ALL)
            else
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.GROUP)
        }
    }

    override fun selectSecret() {
        viewModelScope.launch {

            if (filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.SECRET)
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.ALL)
            else
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.SECRET)
        }
    }

    override fun selectTreasure() {
        viewModelScope.launch {
            if (filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.TREASURE)
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.ALL)
            else
                _filterCapsuleSelect.emit(HomeViewModel.CapsuleFilter.TREASURE)
        }
    }

    override fun clickFollowBtn() {
        viewModelScope.launch {
            _followLocation.emit(!followLocation.value)
        }
    }

    override fun clickNotificationBtn() {
        viewModelScope.launch {
            _homeEvents.emit(HomeViewModel.HomeEvent.GoNotification)
        }
    }

    override fun clickFAB() {
        viewModelScope.launch { _isClickedFAB.emit(!isClickedFAB.value) }
    }

    override fun clickFriendsDisplay() {
        viewModelScope.launch { _isFriendsCapsuleDisplay.emit(!isFriendsCapsuleDisplay.value) }
    }

    override fun resetNearbyCapsules() {
        viewModelScope.launch {
            _capsuleList.emit(emptyList())
        }
    }

    override fun getNearbyMyCapsules(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ) {
        viewModelScope.launch {
            nearbyMyCapsulesHomeUseCase(
                latitude,
                longitude,
                distance,
                capsuleType
            ).collect { result ->
                result.onSuccess {
                    _capsuleList.emit(it.capsuleMarkers)
                }.onFail {
                    if (it == 400){
                        homeEvent(HomeViewModel.HomeEvent.ShowToast("현재 위치가 대한민국 내에 있는지 확인해주세요. 대한민국 내에서만 주변 캡슐을 조회할 수 있습니다."))
                    }else{
                        homeEvent(HomeViewModel.HomeEvent.ShowToast("주변 캡슐 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                    }
                }
            }
        }
    }

    override fun getNearbyFriendsCapsules(latitude: Double, longitude: Double, distance: Double) {
        viewModelScope.launch {
            nearbyFriendsCapsulesHomeUseCase(
                latitude,
                longitude,
                distance,
            ).collect { result ->
                result.onSuccess {
                    _capsuleList.emit(capsuleList.value + it.capsuleMarkers)
                }.onFail {
                    if (it == 400){
                        homeEvent(HomeViewModel.HomeEvent.ShowToast("현재 위치가 대한민국 내에 있는지 확인해주세요. 대한민국 내에서만 주변 캡슐을 조회할 수 있습니다."))
                    }else{
                        homeEvent(HomeViewModel.HomeEvent.ShowToast("주변 캡슐 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                    }
                }
            }
        }
    }

    override fun getNearbyMyAndFriendsCapsules(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ) {
        viewModelScope.launch {
            nearbyMyCapsulesHomeUseCase(
                latitude,
                longitude,
                distance,
                capsuleType
            ).collect { result ->
                result.onSuccess {
                    _capsuleList.emit(it.capsuleMarkers)
                    nearbyFriendsCapsulesHomeUseCase(
                        latitude,
                        longitude,
                        distance,
                    ).collect { result ->
                        result.onSuccess {
                            _capsuleList.emit(capsuleList.value + it.capsuleMarkers)
                        }.onFail {
                            if (it == 400){
                                homeEvent(HomeViewModel.HomeEvent.ShowToast("현재 위치가 대한민국 내에 있는지 확인해주세요. 대한민국 내에서만 주변 캡슐을 조회할 수 있습니다."))
                            }else{
                                homeEvent(HomeViewModel.HomeEvent.ShowToast("주변 캡슐 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                            }
                        }
                    }
                }.onFail {
                    if (it == 400){
                        homeEvent(HomeViewModel.HomeEvent.ShowToast("현재 위치가 대한민국 내에 있는지 확인해주세요. 대한민국 내에서만 주변 캡슐을 조회할 수 있습니다."))
                    }else{
                        homeEvent(HomeViewModel.HomeEvent.ShowToast("주변 캡슐 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                    }
                }
            }
        }
    }
}