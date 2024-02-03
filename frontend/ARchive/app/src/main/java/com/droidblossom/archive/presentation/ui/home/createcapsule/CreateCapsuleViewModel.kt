package com.droidblossom.archive.presentation.ui.home.createcapsule

import com.droidblossom.archive.domain.model.common.AddressData
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.domain.model.common.FileName
import com.droidblossom.archive.domain.model.common.Location
import com.droidblossom.archive.domain.model.common.Skin
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface CreateCapsuleViewModel {
    var groupTypeInt: Int
    val capsuleTypeCreateIs: StateFlow<CapsuleTypeCreate>

    //Create1
    val create1Events: SharedFlow<Create1Event>
    val groupId: StateFlow<Int>

    //Create2
    val create2Events: SharedFlow<Create2Event>
    val skinId: StateFlow<Long>
    val skins : StateFlow<List<Skin>>
    val isSearchOpen : StateFlow<Boolean>

    //Create3
    val create3Events: SharedFlow<Create3Event>
    val capsuleTitle: MutableStateFlow<String>
    val capsuleContent: MutableStateFlow<String>
    val capsuleLocationName: StateFlow<String>
    val capsuleDueDate: MutableStateFlow<String>
    val capsuleLocation : StateFlow<Location>
    val capsuleImg : StateFlow<List<FileName>>
    val capsuleImgUrls: StateFlow<List<String>>
    val isSelectTimeCapsule : StateFlow<Boolean>
    val imgUris : StateFlow<List<Dummy>>
    val imageFiles: StateFlow<List<File>>
    val videoFiles: StateFlow<List<File>>

    //DatePicker
    val year : MutableStateFlow<Int>
    val month : MutableStateFlow<Int>
    val day : MutableStateFlow<Int>
    val hour : MutableStateFlow<Int>
    val min : MutableStateFlow<Int>
    val isSelectTime : StateFlow<Boolean>

    val capsuleLongitude : StateFlow<Double>
    val capsuleLatitude : StateFlow<Double>
    val dueTime : StateFlow<String>
    val address : StateFlow<AddressData>

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
    fun coordToAddress(latitude: Double, longitude: Double)
    fun getDueTime(tiem:String)
    fun getUploadUrls(getS3UrlData : S3UrlRequest)

    fun makeFiles(files : List<File>)

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
        data class ShowToastMessage(val message : String) : Create3Event()
    }

    enum class CapsuleTypeCreate(val title: String) {
        SECRET("SECRETE"), GROUP("GROUP"), PUBLIC("PUBLIC")
    }
}