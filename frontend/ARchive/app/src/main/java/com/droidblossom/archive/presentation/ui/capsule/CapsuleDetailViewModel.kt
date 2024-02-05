package com.droidblossom.archive.presentation.ui.capsule

import com.droidblossom.archive.domain.model.secret.SecretCapsuleDetail
import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CapsuleDetailViewModel {

    val detailEvents : SharedFlow<DetailEvent>

    val capsuleDetail :StateFlow<SecretCapsuleDetail>
    fun getSecretCapsuleDetail(id:Long)

    sealed class DetailEvent {
        data class ShowToastMessage(val message : String) : DetailEvent()
    }
}