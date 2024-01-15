package com.droidblossom.archive.presentation.ui.home.createcapsule

import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCapsuleViewModelImpl @Inject constructor(

) : BaseViewModel(), CreateCapsuleViewModel {
    override var isGroupCapsuleCreate: Boolean = true

}