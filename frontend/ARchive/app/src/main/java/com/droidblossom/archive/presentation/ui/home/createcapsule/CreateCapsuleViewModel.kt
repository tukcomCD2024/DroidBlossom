package com.droidblossom.archive.presentation.ui.home.createcapsule

import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.domain.model.common.FileName
import com.droidblossom.archive.domain.model.common.Location
import com.droidblossom.archive.domain.model.common.Skin
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CreateCapsuleViewModel {
    var groupTypeInt: Int
    val capsuleType: StateFlow<CapsuleType>

    //Create1
    val create1Events: SharedFlow<Create1Event>
    val groupId: StateFlow<Int>

    //Create2
    val create2Events: SharedFlow<Create2Event>
    val skinId: StateFlow<Int>
    val skins : StateFlow<List<Skin>>
    val isSearchOpen : StateFlow<Boolean>

    //Create3
    val create3Events: SharedFlow<Create3Event>
    val capsuleTitle: MutableStateFlow<String>
    val capsuleContent: MutableStateFlow<String>
    val capsuleLocationName: StateFlow<String>
    val capsuleDueDate: StateFlow<String>
    val capsuleLocation : StateFlow<Location>
    val capsuleImg : StateFlow<List<FileName>>
    val capsuleImgUrls: StateFlow<List<String>>
    val isSelectTimeCapsule : StateFlow<Boolean>
    val imgUris : StateFlow<List<Dummy>>

    //DatePicker
    val year : MutableStateFlow<Int>
    val month : MutableStateFlow<Int>
    val day : MutableStateFlow<Int>
    val hour : MutableStateFlow<Int>
    val min : MutableStateFlow<Int>
    val isSelectTime : StateFlow<Boolean>

    fun move1To2()
    fun choseCapsuleType(type: Int)
    fun move2To3()
    fun openSearchSkin()
    fun closeSearchSkin()
    fun searchSkin()
    fun changeSkin(skin: Skin)
    fun moveFinish()
    fun moveLocation()
    fun moveDate()
    fun goSelectTime()
    fun moveImgUpLoad()
    fun moveSingleImgUpLoad()
    fun selectTimeCapsule()
    fun selectCapsule()
    fun addImgUris(list: List<Dummy>)
    fun submitUris(list:List<Dummy>)
    fun coordToAddress(x: String, y: String)

    sealed class Create1Event {
        object NavigateTo2 : Create1Event()
    }

    sealed class Create2Event {
        object NavigateTo3 : Create2Event()

    }

    sealed class Create3Event {
        object ClickFinish : Create3Event()
        object ClickLocation : Create3Event()
        object ClickDate : Create3Event()
        object ClickImgUpLoad : Create3Event()
        object CLickSingleImgUpLoad : Create3Event()
    }

    enum class CapsuleType(val title: String) {
        SECRET("SECRET"), GROUP("GROUP"), OPEN("OPEN")
    }
}