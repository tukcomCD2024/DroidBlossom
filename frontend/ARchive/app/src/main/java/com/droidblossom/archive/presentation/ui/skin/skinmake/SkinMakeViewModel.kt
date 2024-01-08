package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow

interface SkinMakeViewModel {
    val imgUri: MutableStateFlow<Uri?>
}