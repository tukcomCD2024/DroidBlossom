package com.droidblossom.archive.presentation.ui.home.createcapsule

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.domain.model.common.FileName
import com.droidblossom.archive.domain.model.common.Location
import com.droidblossom.archive.domain.model.common.Skin
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
    override var groupTypeInt: Int = 1
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
    private val _skins = MutableStateFlow(
        listOf(Skin(1, "", false), Skin(2, "", false), Skin(3, "", false))
    )
    override val skins: StateFlow<List<Skin>>
        get() = _skins

    private val _isSearchOpen = MutableStateFlow(false)
    override val isSearchOpen: StateFlow<Boolean>
        get() = _isSearchOpen


    //create3
    private val _create3Events = MutableSharedFlow<CreateCapsuleViewModel.Create3Event>()
    override val create3Events: SharedFlow<CreateCapsuleViewModel.Create3Event>
        get() = _create3Events.asSharedFlow()
    override val capsuleTitle: MutableStateFlow<String> = MutableStateFlow("")

    override val capsuleContent: MutableStateFlow<String> = MutableStateFlow("")

    private val _capsuleLocationName = MutableStateFlow("")
    override val capsuleLocationName: StateFlow<String>
        get() = _capsuleLocationName

    private val _capsuleDueDate = MutableStateFlow("")
    override val capsuleDueDate: StateFlow<String>
        get() = _capsuleDueDate

    private val _capsuleLocation = MutableStateFlow(Location(0.0, 0.0))
    override val capsuleLocation: StateFlow<Location>
        get() = _capsuleLocation

    private val _capsuleImg = MutableStateFlow(listOf<FileName>())
    override val capsuleImg: StateFlow<List<FileName>>
        get() = _capsuleImg

    private val _capsuleImgUrls = MutableStateFlow(listOf<String>())
    override val capsuleImgUrls: StateFlow<List<String>>
        get() = _capsuleImgUrls
    private val _isSelectTimeCapsule = MutableStateFlow(true)
    override val isSelectTimeCapsule: StateFlow<Boolean>
        get() = _isSelectTimeCapsule

    private val _imgUris = MutableStateFlow(listOf<Dummy>(Dummy(null, true)))
    override val imgUris: StateFlow<List<Dummy>>
        get() = _imgUris

    //create1
    override fun move1To2() {
        viewModelScope.launch {
            _create1Events.emit(CreateCapsuleViewModel.Create1Event.NavigateTo2)
        }
    }

    override fun choseCapsuleType(type: Int) {
        viewModelScope.launch {
            when (groupTypeInt) {
                1 -> {_capsuleType.emit(CreateCapsuleViewModel.CapsuleType.GROUP)}
                2 -> {_capsuleType.emit(CreateCapsuleViewModel.CapsuleType.OPEN)}
                3 -> {_capsuleType.emit(CreateCapsuleViewModel.CapsuleType.SECRET)}
                else -> {}
            }
        }
    }


    //create2
    override fun move2To3() {
        viewModelScope.launch {
            _skinId.emit(_skins.value.find { it.isClicked }?.skinId ?: 0)
        }
        viewModelScope.launch {
            _create2Events.emit(CreateCapsuleViewModel.Create2Event.NavigateTo3)
        }
    }

    override fun openSearchSkin() {
        viewModelScope.launch {
            _isSearchOpen.emit(true)
        }
    }

    override fun closeSearchSkin() {
        viewModelScope.launch {
            _isSearchOpen.emit(false)
        }
    }

    override fun searchSkin() {
        viewModelScope.launch {
            //스킨 검색 API
        }
    }

    override fun changeSkin(skin: Skin) {
        val submitList = skins.value
        submitList.map { it.isClicked = false }
        submitList[submitList.indexOf(skin)].isClicked = true
        viewModelScope.launch {
            _skins.emit(submitList)
        }
    }


    //creat3
    override fun moveFinish() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickFinish)
        }
    }

    override fun moveLocation() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickLocation)
        }
    }

    override fun moveDate() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickDate)
        }
    }

    override fun moveImgUpLoad() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickImgUpLoad)
        }
    }

    override fun moveSingleImgUpLoad() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.CLickSingleImgUpLoad)
        }
    }

    override fun selectTimeCapsule() {
        viewModelScope.launch { _isSelectTimeCapsule.emit(true) }
    }

    override fun selectCapsule() {
        viewModelScope.launch { _isSelectTimeCapsule.emit(false) }
    }

    override fun addImgUris(list: List<Dummy>) {
        val submitList = list + imgUris.value
        if (submitList.size > 5) {
            val listSize5 = submitList.slice(0..4)
            viewModelScope.launch { _imgUris.emit(listSize5) }
        } else {
            viewModelScope.launch { _imgUris.emit(submitList) }
        }
    }

    override fun submitUris(list: List<Dummy>) {
        val submitList = if (list.none { it.last }) {
            list + listOf(Dummy(null, true))
        } else list
        viewModelScope.launch { _imgUris.emit(submitList) }
    }
}