package com.droidblossom.archive.presentation.ui.capsule

import com.droidblossom.archive.domain.model.common.CapsuleDetail
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CapsuleDetailViewModel {

    val detailEvents : SharedFlow<DetailEvent>

    val capsuleDetail :StateFlow<CapsuleDetail>
    fun getSecretCapsuleDetail(id:Long)
    fun getPublicCapsuleDetail(id:Long)
    fun getGroupCapsuleDetail(id:Long)

    sealed class DetailEvent {
        data class ShowToastMessage(val message : String) : DetailEvent()
    }
}