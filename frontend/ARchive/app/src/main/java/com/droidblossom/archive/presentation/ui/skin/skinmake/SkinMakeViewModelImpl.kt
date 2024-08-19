package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.R
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsMakeRequest
import com.droidblossom.archive.domain.model.capsule_skin.SkinMotion
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinsMakeUseCase
import com.droidblossom.archive.domain.usecase.s3.S3UrlsGetUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModelImpl
import com.droidblossom.archive.util.Motion
import com.droidblossom.archive.util.Retarget
import com.droidblossom.archive.util.S3Util
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SkinMakeViewModelImpl @Inject constructor(
    private val s3UrlsGetUseCase: S3UrlsGetUseCase,
    private val capsuleSkinsMakeUseCase: CapsuleSkinsMakeUseCase,
    private val s3Util: S3Util
) : BaseViewModel(), SkinMakeViewModel {

    companion object {
        const val S3DIRECTORY = "capsuleSkin"
        const val IMAGEEXTENSION = "image/jpeg"
    }

    private val _skinMakeEvents = MutableSharedFlow<SkinMakeViewModel.SkinMakeEvent>()
    override val skinMakeEvents: SharedFlow<SkinMakeViewModel.SkinMakeEvent> =
        _skinMakeEvents.asSharedFlow()

    override val skinImgUri = MutableStateFlow<Uri?>(null)

    private val _addMotion = MutableStateFlow(false)
    override val addMotion: StateFlow<Boolean> get() = _addMotion

    private val _skinImgFile = MutableStateFlow<File?>(null)
    override val skinImgFile: StateFlow<File?> = _skinImgFile

    private val _skinMotions = MutableStateFlow<List<SkinMotion>>(
        listOf(
            SkinMotion(
                1L,
                "https://github.com/comst19/GradProjectHub/blob/main/db7457be-b654-48ac-8b20-b47c3644fcab_1.gif?raw=true",
                Motion.JUMPING_JACKS,
                Retarget.CMU,
                false
            ),
            SkinMotion(
                2L,
                "https://github.com/comst19/GradProjectHub/blob/main/7a87f9b0-19dd-4ae8-b88e-3e1e142c5122_2.gif?raw=true",
                Motion.DAB,
                Retarget.FAIR,
                false
            ),
            SkinMotion(
                3L,
                "https://github.com/comst19/GradProjectHub/blob/main/45696698-7cad-4838-a947-2b07cb5b2c05_3.gif?raw=true",
                Motion.JUMPING,
                Retarget.FAIR,
                false
            ),
            SkinMotion(
                4L,
                "https://github.com/comst19/GradProjectHub/blob/main/b8112cd6-48fb-42de-b326-80efa4d20150_4.gif?raw=true",
                Motion.WAVE_HELLO,
                Retarget.FAIR,
                false
            ),
            SkinMotion(
                5L,
                "https://github.com/comst19/GradProjectHub/blob/main/02a9fb2b-f126-461c-886a-5f232e93c12b_5.gif?raw=true",
                Motion.ZOMBIE,
                Retarget.FAIR,
                false
            ),
            SkinMotion(
                6L,
                "https://github.com/comst19/GradProjectHub/blob/main/fd145d45-ba3b-41b3-9e58-6f089a4267a0_6.gif?raw=true",
                Motion.JESSE_DANCE,
                Retarget.ROKOKO,
                false
            )
        )
    )
    override val skinMotions get() = _skinMotions

    private val _skinMotionIndex = MutableStateFlow<Int>(-1)
    override val skinMotionIndex: StateFlow<Int>
        get() = _skinMotionIndex

    private val _firstAddMotionClick = MutableStateFlow(true)
    override val firstAddMotionClick: StateFlow<Boolean> get() = _firstAddMotionClick


    override fun selectSkinMotion(previousPosition: Int?, currentPosition: Int) {
        viewModelScope.launch {
            val newList = _skinMotions.value
            previousPosition?.let {
                newList[it].isClicked = false
            }
            newList[currentPosition].isClicked = true
            _skinMotions.emit(newList)
        }
    }

    override fun skinMakeEvent(event: SkinMakeViewModel.SkinMakeEvent) {
        viewModelScope.launch {
            _skinMakeEvents.emit(event)
        }
    }

    override val skinName = MutableStateFlow("")

    override fun selectAddMotion() {
        if (firstAddMotionClick.value) {
            skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.ShowExampleImage)
        }
        viewModelScope.launch {
            if (addMotion.value) {
                val newList = skinMotions.value.map { skinMotion ->
                    skinMotion.copy(isClicked = false)
                }
                _skinMotions.emit(newList)
                _skinMotionIndex.emit(-1)
            }
            _addMotion.emit(!addMotion.value)
        }
    }

    override fun setFile(skinImgFile: File) {
        _skinImgFile.value = skinImgFile
    }

    override fun makeSkin() {
        viewModelScope.launch {
            val skinImgName: List<String> = listOf(skinImgFile.value!!.name)
            val empty = emptyList<String>()
            val getS3UrlData =
                S3UrlRequest(S3DIRECTORY, skinImgName, empty)
            getUploadUrls(getS3UrlData)

        }
    }

    override fun showExampleImg() {
        skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.ShowExampleImage)
    }

    override fun setFirstAddMotionClick(state: Boolean) {
        _firstAddMotionClick.value = state
    }

    private fun getUploadUrls(getS3UrlData: S3UrlRequest) {
        viewModelScope.launch {
            s3UrlsGetUseCase(getS3UrlData.toDto()).collect { result ->
                result.onSuccess {
                    Log.d("getUploadUrls", "$it")
                    uploadFilesToS3(skinImgFile.value!!, it.preSignedImageUrls[0])
                }.onFail {
                    SkinMakeViewModel.SkinMakeEvent.ShowToastMessage(
                        "스킨 생성을 실패했습니다. " + ARchiveApplication.getString(
                            R.string.reTryMessage
                        )
                    )
                    skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.DismissLoading)
                    Log.d("getUploadUrls", "getUploadUrl 실패")
                }
            }
        }
    }

    private fun uploadFilesToS3(
        skinImgFiles: File,
        preSignedImageUrls: String,
    ) {
        viewModelScope.launch {
            var uploadSuccess = false

            withContext(Dispatchers.IO) {
                try {
                    s3Util.uploadImageWithPresignedUrl(skinImgFiles, preSignedImageUrls)
                    uploadSuccess = true
                } catch (e: Exception) {
                    uploadSuccess = false
                }
            }

            if (uploadSuccess) {
                submitSkin()
            } else {
                skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.DismissLoading)
            }
        }
    }

    private fun submitSkin() {
        if (addMotion.value) {
            val skinMotion = skinMotions.value.find { it.isClicked }
            _skinMotionIndex.value =
                if (skinMotion != null) skinMotions.value.indexOf(skinMotion) else -1
        }
        viewModelScope.launch {
            capsuleSkinsMakeUseCase(
                CapsuleSkinsMakeRequest(
                    skinName.value,
                    skinImgFile.value!!.name,
                    S3DIRECTORY,
                    skinMotions.value.getOrNull(skinMotionIndex.value)?.motionName,
                    skinMotions.value.getOrNull(skinMotionIndex.value)?.retarget
                ).toDto()
            ).collect { result ->
                result.onSuccess {
                    skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.SuccessSkinMake)
                }.onFail {
                    skinMakeEvent(
                        SkinMakeViewModel.SkinMakeEvent.ShowToastMessage(
                            "스킨 생성을 실패했습니다. " + ARchiveApplication.getString(
                                R.string.reTryMessage
                            )
                        )
                    )
                    skinMakeEvent(
                        SkinMakeViewModel.SkinMakeEvent.FinishActivity
                    )
                }
                skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.DismissLoading)
            }
        }
    }
}