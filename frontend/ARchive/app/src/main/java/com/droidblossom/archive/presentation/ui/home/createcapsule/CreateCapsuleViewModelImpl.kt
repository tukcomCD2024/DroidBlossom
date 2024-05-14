package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.common.AddressData
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.domain.model.common.FileName
import com.droidblossom.archive.domain.model.common.Location
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.domain.model.common.CapsuleCreateRequest
import com.droidblossom.archive.domain.usecase.capsule.GetAddressUseCase
import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinsPageUseCase
import com.droidblossom.archive.domain.usecase.open.PublicCapsuleCreateUseCase
import com.droidblossom.archive.domain.usecase.s3.S3UrlsGetUseCase
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleCreateUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.S3Util
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CreateCapsuleViewModelImpl @Inject constructor(
    private val getAddressUseCase: GetAddressUseCase,
    private val secretCapsuleCreateUseCase: SecretCapsuleCreateUseCase,
    private val publicCapsuleCreateUseCase: PublicCapsuleCreateUseCase,
    private val s3UrlsGetUseCase: S3UrlsGetUseCase,
    private val capsuleSkinsPageUseCase: CapsuleSkinsPageUseCase,
    private val s3Util: S3Util,
) : BaseViewModel(), CreateCapsuleViewModel {

    companion object {
        const val S3DIRECTORY = "capsuleContents"
        const val IMAGEEXTENSION = "image/jpeg"
    }

    override var groupTypeInt: Int = 1
    private val _capsuleTypeCreateIs =
        MutableStateFlow(CreateCapsuleViewModel.CapsuleTypeCreate.SECRET)
    override val capsuleTypeCreateIs: StateFlow<CreateCapsuleViewModel.CapsuleTypeCreate>
        get() = _capsuleTypeCreateIs

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
    private val _skinId = MutableStateFlow<Long>(0)
    override val skinId: StateFlow<Long>
        get() = _skinId
    private val _skins = MutableStateFlow(listOf<CapsuleSkinSummary>())
    override val skins: StateFlow<List<CapsuleSkinSummary>>
        get() = _skins

    private val _isSearchOpen = MutableStateFlow(false)
    override val isSearchOpen: StateFlow<Boolean>
        get() = _isSearchOpen

    private val _lastCreatedSkinTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedSkinTime: StateFlow<String>
        get() = _lastCreatedSkinTime

    private val _hasNextSkins = MutableStateFlow(true)
    override val hasNextSkins: StateFlow<Boolean>
        get() = _hasNextSkins


    //create3
    private val _isNotSelectCapsule = MutableStateFlow(true)
    override val isNotSelectCapsule: StateFlow<Boolean>
        get() = _isNotSelectCapsule

    private val _create3Events = MutableSharedFlow<CreateCapsuleViewModel.Create3Event>()
    override val create3Events: SharedFlow<CreateCapsuleViewModel.Create3Event>
        get() = _create3Events.asSharedFlow()

    override val capsuleTitle: MutableStateFlow<String> = MutableStateFlow("")

    override val capsuleContent: MutableStateFlow<String> = MutableStateFlow("")

    private val _capsuleLocationName = MutableStateFlow("")
    override val capsuleLocationName: StateFlow<String>
        get() = _capsuleLocationName

    override val capsuleDueDate: MutableStateFlow<String> = MutableStateFlow("")

    private val _capsuleLocation = MutableStateFlow(Location(0.0, 0.0))
    override val capsuleLocation: StateFlow<Location>
        get() = _capsuleLocation

    private val _capsuleImg = MutableStateFlow(listOf<FileName>())
    override val capsuleImg: StateFlow<List<FileName>>
        get() = _capsuleImg

    private val _capsuleImgUrls = MutableStateFlow(listOf<String>())
    override val capsuleImgUrls: StateFlow<List<String>>
        get() = _capsuleImgUrls
    private val _isSelectTimeCapsule = MutableStateFlow(false)
    override val isSelectTimeCapsule: StateFlow<Boolean>
        get() = _isSelectTimeCapsule

    private val _imgUris = MutableStateFlow(listOf<Dummy>(Dummy(null, null, true)))
    override val imgUris: StateFlow<List<Dummy>>
        get() = _imgUris

    private val _contentUris = MutableStateFlow(listOf<Dummy>(Dummy(null, null, true)))
    override val contentUris: StateFlow<List<Dummy>>
        get() = _contentUris

    private val _videoUri = MutableStateFlow(listOf<Uri>())
    override val videoUri: StateFlow<List<Uri>>
        get() = _videoUri

    private val _imageFiles = MutableStateFlow<List<File>>(emptyList())
    override val imageFiles: StateFlow<List<File>> = _imageFiles

    private val _videoFiles = MutableStateFlow<List<File>>(emptyList())
    override val videoFiles: StateFlow<List<File>> = _videoFiles

    private val _isOpenTimeSetting = MutableStateFlow(false)
    override val isOpenTimeSetting: StateFlow<Boolean>
        get() = _isOpenTimeSetting

    //dateDialog
    override val year = MutableStateFlow<Int>(DateUtils.getCurrentYear())
    override val month: MutableStateFlow<Int> = MutableStateFlow<Int>(DateUtils.getCurrentMonth())
    override val day: MutableStateFlow<Int> = MutableStateFlow<Int>(DateUtils.getCurrentDay())
    override val hour: MutableStateFlow<Int> = MutableStateFlow<Int>(DateUtils.getCurrentHour())
    override val min: MutableStateFlow<Int> = MutableStateFlow<Int>(DateUtils.getCurrentMin())
    private val _isSelectTime = MutableStateFlow<Boolean>(false)
    override val isSelectTime: StateFlow<Boolean>
        get() = _isSelectTime

    private val _capsuleLongitude = MutableStateFlow(0.0)
    override val capsuleLongitude: StateFlow<Double>
        get() = _capsuleLongitude

    private val _capsuleLatitude = MutableStateFlow(0.0)
    override val capsuleLatitude: StateFlow<Double>
        get() = _capsuleLatitude
    private val _dueTime = MutableStateFlow("")
    override val dueTime: StateFlow<String>
        get() = _dueTime
    private val _address = MutableStateFlow(AddressData())
    override val address: StateFlow<AddressData>
        get() = _address

    private var imageNames = listOf<String>()
    private var videoNames = listOf<String>()

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow =
        scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getLstJob: Job? = null

    init {
        viewModelScope.launch {
            scrollEventFlow.collect {
                getSkinList()
            }
        }
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }


    //create1
    override fun move1To2() {
        viewModelScope.launch {
            _create1Events.emit(CreateCapsuleViewModel.Create1Event.NavigateTo2)
        }
    }

    override fun choseCapsuleType(type: Int) {
        viewModelScope.launch {
            when (groupTypeInt) {
                1 -> {
                    _capsuleTypeCreateIs.emit(CreateCapsuleViewModel.CapsuleTypeCreate.GROUP)
                }

                2 -> {
                    _capsuleTypeCreateIs.emit(CreateCapsuleViewModel.CapsuleTypeCreate.PUBLIC)
                }

                3 -> {
                    _capsuleTypeCreateIs.emit(CreateCapsuleViewModel.CapsuleTypeCreate.SECRET)
                }

                else -> {}
            }
        }
    }


    //create2
    override fun move2To3() {
        viewModelScope.launch {
            if (_skins.value.find { it.isClicked } == null) {
                _create2Events.emit(CreateCapsuleViewModel.Create2Event.ShowToastMessage("스킨은 필수입니다."))
            } else {
                _skinId.emit(_skins.value.find { it.isClicked }!!.id)
                _create2Events.emit(CreateCapsuleViewModel.Create2Event.NavigateTo3)
            }
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

    override fun changeSkin(previousPosition: Int?, currentPosition: Int) {
        viewModelScope.launch {
            val newList = _skins.value
            previousPosition?.let {
                newList[it].isClicked = false
            }
            newList[currentPosition].isClicked = true
            _skins.emit(newList)
        }
    }

    override fun getSkinList() {
        if (hasNextSkins.value) {
            getLstJob?.cancel()
            getLstJob = viewModelScope.launch {
                capsuleSkinsPageUseCase(
                    PagingRequestDto(
                        15,
                        _lastCreatedSkinTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextSkins.emit(it.hasNext)
                        if (skins.value.isEmpty()) {
                            _skins.emit(it.skins)
                        } else {
                            _skins.emit(skins.value + it.skins)
                        }
                        _lastCreatedSkinTime.value = _skins.value.last().createdAt
                    }.onFail {
                        _create2Events.emit(CreateCapsuleViewModel.Create2Event.ShowToastMessage("스킨 불러오기 실패."))
                    }

                }
            }
        }
    }


    //creat3
    override fun moveFinish() {
        Log.d("캡슐생성", "${capsuleTypeCreateIs.value.title},${address.value},${dueTime.value}")
        viewModelScope.launch {
            if (isSelectTimeCapsule.value && (capsuleLatitude.value == 0.0 || capsuleTitle.value.isEmpty() || capsuleContent.value.isEmpty() || dueTime.value.isEmpty())) {
                _create3Events.emit(CreateCapsuleViewModel.Create3Event.ShowToastMessage("타임캡슐은 시간, 제목, 내용이 필수 입니다."))
                return@launch
            }
            if (!isSelectTimeCapsule.value && (capsuleLatitude.value == 0.0 || capsuleTitle.value.isEmpty() || capsuleContent.value.isEmpty())) {
                _create3Events.emit(CreateCapsuleViewModel.Create3Event.ShowToastMessage("캡슐은 제목, 내용이 필수 입니다."))
                return@launch
            }

            if (imageFiles.value.isEmpty() && videoFiles.value.isEmpty()) {
                imageNames = listOf<String>()
                videoNames = listOf<String>()

            } else if (imageFiles.value.isNotEmpty() && videoFiles.value.isEmpty()) {
                imageNames = imageFiles.value.map { file ->
                    file.name
                }
            } else if (imageFiles.value.isEmpty() && videoFiles.value.isNotEmpty()) {
                videoNames = videoFiles.value.map { file ->
                    file.name
                }
            } else {
                imageNames = imageFiles.value.map { file ->
                    file.name
                }
                videoNames = videoFiles.value.map { file ->
                    file.name
                }
            }
            if (imageNames.isNotEmpty() || videoNames.isNotEmpty()) {
                val getS3UrlData = S3UrlRequest(S3DIRECTORY, imageNames, videoNames)
                getUploadUrls(getS3UrlData)
            } else {
                createCapsule()
            }

        }
    }

    override fun setFiles(imageFiles: List<File>, videoFiles: List<File>) {
        _imageFiles.value = imageFiles
        _videoFiles.value = videoFiles
    }

    override fun closeTimeSetting() {
        _isOpenTimeSetting.value = false
    }

    override fun openTimeSetting() {
        _isOpenTimeSetting.value = true
    }

    override fun moveLocation() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickLocation)
        }
    }


    override fun coordToAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _capsuleLatitude.value = latitude
            _capsuleLongitude.value = longitude
            getAddressUseCase(latitude, longitude).collect { result ->
                result.onSuccess {
                    _capsuleLocationName.emit(if (it.roadName.isNullOrEmpty()) it.fullRoadAddressName else it.roadName)
                    _address.emit(it)
                }.onFail {
                    _capsuleLocationName.emit("위치를 찾을 수 없음")
                }
            }
        }
    }

    override fun getDueTime(time: String) {
        viewModelScope.launch {
            _dueTime.emit(time)
        }
    }

    override fun moveDate() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickDate)
        }
    }

    override fun goSelectTime() {
        viewModelScope.launch {
            _isSelectTime.emit(true)
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

    override fun moveVideoUpLoad() {
        viewModelScope.launch {
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickVideoUpLoad)
        }
    }

    override fun selectTimeCapsule() {
        viewModelScope.launch {
            _isSelectTimeCapsule.emit(true)
            _isNotSelectCapsule.emit(false)
            _isOpenTimeSetting.emit(true)
        }
    }

    override fun selectCapsule() {
        viewModelScope.launch {
            _isSelectTimeCapsule.emit(false)
            _dueTime.emit("")
            capsuleDueDate.emit("")
            _isOpenTimeSetting.emit(false)
        }
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

    override fun addContentUris(list: List<Dummy>) {
        val submitList = list + contentUris.value
        if (submitList.size > 5) {
            val listSize5 = submitList.slice(0..4)
            viewModelScope.launch { _contentUris.emit(listSize5) }
        } else {
            viewModelScope.launch { _contentUris.emit(submitList) }
        }
    }

    override fun submitUris(list: List<Dummy>) {
        val submitList = if (list.none { it.last }) {
            list + listOf(Dummy(null, null, true))
        } else list
        viewModelScope.launch { _imgUris.emit(submitList) }
    }

    override fun submitContentUris(list: List<Dummy>) {
        val submitList = if (list.none { it.last }) {
            list + listOf(Dummy(null, null, true))
        } else list
        viewModelScope.launch { _contentUris.emit(submitList) }
    }

    override fun deleteVideoUrl() {
        viewModelScope.launch {
            _videoUri.value = listOf()
        }
    }

    override fun addVideoUrl(uri: Uri) {
        viewModelScope.launch {
            _videoUri.value = listOf(uri)
        }
    }

    override fun getUploadUrls(getS3UrlData: S3UrlRequest) {
        viewModelScope.launch {
            s3UrlsGetUseCase(getS3UrlData.toDto()).collect { result ->
                result.onSuccess {
                    Log.d("getUploadUrls", "$it")
                    uploadFilesToS3(
                        imageFiles.value,
                        videoFiles.value,
                        it.preSignedImageUrls,
                        it.preSignedVideoUrls
                    )
                }.onFail {
                    _create3Events.emit(CreateCapsuleViewModel.Create3Event.DismissLoading)
                    Log.d("getUploadUrls", "getUploadUrl 실패")
                }
            }
        }
    }

    private fun uploadFilesToS3(
        imageFiles: List<File>,
        videoFiles: List<File>,
        preSignedImageUrls: List<String>,
        preSignedVideoUrls: List<String>
    ) {
        viewModelScope.launch {
            val uploadResults = mutableListOf<Deferred<Boolean>>()

            imageFiles.zip(preSignedImageUrls).forEach { (imageFile, url) ->
                val uploadJob = async(Dispatchers.IO) {
                    try {
                        s3Util.uploadImageWithPresignedUrl(imageFile, url)
                        Log.d(
                            "uploadFilesToS3",
                            "Image file ${imageFile.name} uploaded successfully"
                        )
                        true
                    } catch (e: Exception) {
                        Log.e(
                            "uploadFilesToS3",
                            "Failed to upload image file ${imageFile.name}: ${e.message}"
                        )
                        false
                    }
                }
                uploadResults.add(uploadJob)
            }

            // 비디오 파일 업로드
            videoFiles.zip(preSignedVideoUrls).forEach { (videoFile, url) ->
                val uploadJob = async(Dispatchers.IO) {
                    try {
                        s3Util.uploadVideoWithPresignedUrl(videoFile, url)
                        Log.d(
                            "uploadFilesToS3",
                            "Video file ${videoFile.name} uploaded successfully"
                        )
                        true
                    } catch (e: Exception) {
                        Log.e(
                            "uploadFilesToS3",
                            "Failed to upload video file ${videoFile.name}: ${e.message}"
                        )
                        false
                    }
                }
                uploadResults.add(uploadJob)
            }

            val allUploadsSuccessful = uploadResults.awaitAll().all { it }

            if (allUploadsSuccessful) {
                Log.d("uploadFilesToS3", "All files uploaded successfully")
                createCapsule()
            } else {
                _create3Events.emit(CreateCapsuleViewModel.Create3Event.DismissLoading)
                Log.e("uploadFilesToS3", "One or more file uploads failed")

            }
        }
    }

    private fun createCapsule() {
        viewModelScope.launch {
            when (capsuleTypeCreateIs.value) {
                CreateCapsuleViewModel.CapsuleTypeCreate.PUBLIC -> {
                    publicCapsuleCreateUseCase(
                        CapsuleCreateRequest(
                            capsuleSkinId = skinId.value,
                            content = capsuleContent.value,
                            directory = S3DIRECTORY,
                            dueDate = dueTime.value,
                            imageNames = imageNames,
                            videoNames = videoNames,
                            addressData = address.value,
                            latitude = capsuleLatitude.value,
                            longitude = capsuleLongitude.value,
                            title = capsuleTitle.value,
                        )
                    ).collect { result ->
                        result.onSuccess {
                            _create3Events.emit(
                                CreateCapsuleViewModel.Create3Event.ShowToastMessage(
                                    "캡슐이 생성되었습니다."
                                )
                            )
                            Log.d("캡슐생성", "$it")
                        }.onFail {
                            _create3Events.emit(
                                CreateCapsuleViewModel.Create3Event.ShowToastMessage(
                                    "캡슐이 생성 실패했습니다.."
                                )
                            )
                        }
                    }
                }

                CreateCapsuleViewModel.CapsuleTypeCreate.SECRET -> {
                    secretCapsuleCreateUseCase(
                        CapsuleCreateRequest(
                            capsuleSkinId = skinId.value,
                            content = capsuleContent.value,
                            directory = S3DIRECTORY,
                            dueDate = dueTime.value,
                            imageNames = imageNames,
                            videoNames = videoNames,
                            addressData = address.value,
                            latitude = capsuleLatitude.value,
                            longitude = capsuleLongitude.value,
                            title = capsuleTitle.value,
                        )
                    ).collect { result ->
                        result.onSuccess {
                            _create3Events.emit(
                                CreateCapsuleViewModel.Create3Event.ShowToastMessage(
                                    "캡슐이 생성되었습니다."
                                )
                            )
                            Log.d("캡슐생성", "$it")
                        }.onFail {
                            _create3Events.emit(
                                CreateCapsuleViewModel.Create3Event.ShowToastMessage(
                                    "캡슐이 생성 실패했습니다.."
                                )
                            )
                        }
                    }
                }

                CreateCapsuleViewModel.CapsuleTypeCreate.GROUP -> {

                }
            }
            _create3Events.emit(CreateCapsuleViewModel.Create3Event.ClickFinish)
        }
    }

}