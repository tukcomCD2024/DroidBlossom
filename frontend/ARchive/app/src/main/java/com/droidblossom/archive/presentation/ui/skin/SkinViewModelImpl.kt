package com.droidblossom.archive.presentation.ui.skin

import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinsPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SkinViewModelImpl @Inject constructor(
    private val getSkinsPageUseCase: CapsuleSkinsPageUseCase
):BaseViewModel(), SkinViewModel {

    

}