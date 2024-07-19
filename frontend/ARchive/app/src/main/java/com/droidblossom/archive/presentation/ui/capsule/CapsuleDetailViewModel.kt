package com.droidblossom.archive.presentation.ui.capsule

import com.droidblossom.archive.domain.model.common.CapsuleDetail
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CapsuleDetailViewModel {

    val detailEvents : SharedFlow<DetailEvent>

    val capsuleDetail :StateFlow<CapsuleDetail>
    val removeCapsule: StateFlow<Boolean>

    fun getSecretCapsuleDetail(id:Long)
    fun getPublicCapsuleDetail(id:Long)
    fun getGroupCapsuleDetail(id:Long)
    fun deleteCapsule(id: Long, capsuleType: String)
    fun reportCapsule(id: Long)

    sealed class DetailEvent {
        data class ShowToastMessage(val message : String) : DetailEvent()
        object FinishActivity : DetailEvent()
        object ShowLoading : DetailEvent()
        object DismissLoading : DetailEvent()
    }
}