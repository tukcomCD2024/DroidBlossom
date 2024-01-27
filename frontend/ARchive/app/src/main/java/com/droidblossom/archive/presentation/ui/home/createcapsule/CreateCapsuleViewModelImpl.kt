package com.droidblossom.archive.presentation.ui.home.createcapsule

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.FileName
import com.droidblossom.archive.domain.model.common.Location
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCapsuleViewModelImpl @Inject constructor(

) : BaseViewModel(), CreateCapsuleViewModel {
    override var isGroupCapsuleCreate: Boolean = true
    private val _capsuleType = MutableStateFlow(CreateCapsuleViewModel.CapsuleType.SECRET)
    override val capsuleType: StateFlow<CreateCapsuleViewModel.CapsuleType>
        get() = _capsuleType

    //create1

    private val _create1Events = MutableSharedFlow<CreateCapsuleViewModel.Create1Event>()
    override val create1Events: SharedFlow<CreateCapsuleViewModel.Create1Event>
        get() = _create1Events.asSharedFlow()
    private val _groupId = MutableStateFlow(0)
    override val groupId: StateFlow<Int>
        get() = _groupId

    //create2
    private val _create2Events = MutableSharedFlow<CreateCapsuleViewModel.Create2Event>()
    override val create2Events: SharedFlow<CreateCapsuleViewModel.Create2Event>
        get() = _create2Events
    private val _skinId = MutableStateFlow<Int>(0)
    override val skinId: StateFlow<Int>
        get() = _skinId

    //create3
    private val _create3Events = MutableSharedFlow<CreateCapsuleViewModel.Create3Event>()
    override val create3Events: SharedFlow<CreateCapsuleViewModel.Create3Event>
        get() = _create3Events.asSharedFlow()
    private val _capsuleTitle= MutableStateFlow("")
    override val capsuleTitle: StateFlow<String>
        get() = _capsuleTitle

    private val _capsuleContent= MutableStateFlow("")
    override val capsuleContent: StateFlow<String>
        get() = _capsuleContent

    private val _capsuleLocationName = MutableStateFlow("")
    override val capsuleLocationName: StateFlow<String>
        get() = _capsuleLocationName

    private val _capsuleDueDate =  MutableStateFlow("")
    override val capsuleDueDate: StateFlow<String>
        get() = _capsuleDueDate

    private val _capsuleLocation = MutableStateFlow(Location(0.0,0.0))
    override val capsuleLocation: StateFlow<Location>
        get() = _capsuleLocation

    private val _capsuleImg = MutableStateFlow(listOf<FileName>())
    override val capsuleImg: StateFlow<List<FileName>>
        get() = _capsuleImg

    private val _capsuleImgUrls = MutableStateFlow(listOf<String>())
    override val capsuleImgUrls: StateFlow<List<String>>
        get() = _capsuleImgUrls

    override fun move1To2() {
        viewModelScope.launch {
            _create1Events.emit(CreateCapsuleViewModel.Create1Event.NavigateTo2)
        }
    }

    override fun move2To3() {
        viewModelScope.launch {
            _create2Events.emit(CreateCapsuleViewModel.Create2Event.NavigateTo3)
        }
    }

    override fun moveFinish() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickFinish)
        }
    }

}