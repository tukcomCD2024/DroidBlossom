package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.domain.usecase.s3.S3UrlsGetUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.S3Util
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SkinMakeViewModelImpl @Inject constructor(
    private val s3UrlsGetUseCase : S3UrlsGetUseCase,
    private val s3Util: S3Util
) : BaseViewModel(), SkinMakeViewModel {
    override val imgUri = MutableStateFlow<Uri?>(null)
    var signedUrl = ""

    fun getUploadUrl(getS3UrlData : S3UrlRequest, file : File){
        viewModelScope.launch {
            s3UrlsGetUseCase(getS3UrlData.toDto()).collect{result ->
                result.onSuccess {
                    val originalString = it.preSignedUrls.toString()
                    val trimmedString = originalString.trim('[', ']')
                    Log.d("티티","getUploadUrl 성공 : $trimmedString")
                    signedUrl = trimmedString
                    uploadFileToS3(getS3UrlData.fileMetaDataList[0].fileName,file)

                }.onFail {
                    Log.d("티티","getUploadUrl 실패")
                }
            }
        }
    }

    fun uploadFileToS3(fileName : String, file : File){
        viewModelScope.launch(Dispatchers.IO){
            try {
                s3Util.putObjectWithPresignedUrl(fileName,file)
                Log.d("티티", "성공")
            }catch (e:Exception){
                Log.d("티티", "uploadFileToS3 : ${e.message}")

            }
        }
    }
}