package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.net.Uri
import com.droidblossom.archive.domain.model.capsule_skin.SkinMotion
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface SkinMakeViewModel {

    val skinMakeEvents: SharedFlow<SkinMakeEvent>
    val skinName : MutableStateFlow<String>
    val skinImgUri: MutableStateFlow<Uri?>
    val addMotion : StateFlow<Boolean>
    val skinImgFile: StateFlow<File?>
    val skinMotions: MutableList<SkinMotion>

    fun skinMakeEvent(event: SkinMakeEvent)
    fun selectAddMotion()
    fun setFile(skinImgFile: File)
    fun makeSkin()

    sealed class SkinMakeEvent {
        object SuccessSkinMake : SkinMakeEvent()
        data class ShowToastMessage(val message : String) : SkinMakeEvent()
    }
}