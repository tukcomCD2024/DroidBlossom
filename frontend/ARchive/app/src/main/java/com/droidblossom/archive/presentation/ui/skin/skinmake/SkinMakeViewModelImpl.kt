package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsMakeRequest
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinsMakeUseCase
import com.droidblossom.archive.domain.usecase.s3.S3UrlsGetUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModelImpl
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
    override val skinMakeEvents: SharedFlow<SkinMakeViewModel.SkinMakeEvent> = _skinMakeEvents.asSharedFlow()

    override val skinImgUri = MutableStateFlow<Uri?>(null)

    private val _addMotion = MutableStateFlow(false)
    override val addMotion: StateFlow<Boolean> get() = _addMotion

    private val _skinImgFile = MutableStateFlow<File?>(null)
    override val skinImgFile: StateFlow<File?> = _skinImgFile
    override fun skinMakeEvent(event: SkinMakeViewModel.SkinMakeEvent) {
        viewModelScope.launch {
            _skinMakeEvents.emit(event)
        }
    }

    override val skinName = MutableStateFlow("")

    override fun selectAddMotion() {
        viewModelScope.launch {
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
                S3UrlRequest(CreateCapsuleViewModelImpl.S3DIRECTORY, skinImgName, empty)
            getUploadUrls(getS3UrlData)

            capsuleSkinsMakeUseCase(
                CapsuleSkinsMakeRequest(
                    skinName.value,
                    skinImgFile.value!!.name,
                    S3DIRECTORY,
                    null,
                    null
                ).toDto()
            ).collect{ result ->
                result.onSuccess {
                    skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.ShowToastMessage("캡슐 만드는데 소요시간이 있습니다.\n만들어지면 알림이 옵니다."))
                    skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.SuccessSkinMake)
                    Log.d("스킨 생성", "생성 성공")
                }.onFail {
                    Log.d("스킨 생성", "생성 실패")
                }
            }

        }
    }

    private fun getUploadUrls(getS3UrlData: S3UrlRequest) {
        viewModelScope.launch {
            s3UrlsGetUseCase(getS3UrlData.toDto()).collect { result ->
                result.onSuccess {
                    Log.d("getUploadUrls", "$it")
                    uploadFilesToS3(skinImgFile.value!!, it.preSignedImageUrls[0])
                }.onFail {
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

            withContext(Dispatchers.IO) {
                try {
                    Log.d("uploadFilesToS3", "Uploading image file: ${skinImgFiles.name}")
                    s3Util.uploadImageWithPresignedUrl(skinImgFiles, preSignedImageUrls)
                    Log.d(
                        "uploadFilesToS3",
                        "Image file ${skinImgFiles.name} uploaded successfully"
                    )
                } catch (e: Exception) {
                    Log.e(
                        "uploadFilesToS3",
                        "Failed to upload image file ${skinImgFiles.name}: ${e.message}"
                    )
                }
            }

            Log.d("uploadFilesToS3", "All files uploaded successfully")
        }
    }
}