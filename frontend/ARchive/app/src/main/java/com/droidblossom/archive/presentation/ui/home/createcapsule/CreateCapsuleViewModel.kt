package com.droidblossom.archive.presentation.ui.home.createcapsule

import com.droidblossom.archive.presentation.ui.auth.AuthViewModel

interface CreateCapsuleViewModel {
    var isGroupCapsuleCreate : Boolean


    sealed class Create1Event {
        object NavigateTo2 : Create1Event()
    }

    sealed class Create2Event {
        object NavigateTo3 : Create2Event()

        object SearchSkin : Create2Event()
    }
    sealed class Create3Event {
        object Finish : Create3Event()

        object ClickTime : Create3Event()
        object ClickDate : Create3Event()
        object ClickImgUpLoad : Create3Event()
    }
}