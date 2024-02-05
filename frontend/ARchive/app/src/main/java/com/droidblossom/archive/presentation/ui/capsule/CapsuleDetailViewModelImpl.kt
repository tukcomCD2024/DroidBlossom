package com.droidblossom.archive.presentation.ui.capsule

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.secret.SecretCapsuleDetail
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleDetailUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CapsuleDetailViewModelImpl @Inject constructor(
    private val secretCapsuleDetailUseCase: SecretCapsuleDetailUseCase
): BaseViewModel(), CapsuleDetailViewModel {
    private val _capsuleDetail = MutableStateFlow(SecretCapsuleDetail())
    override val capsuleDetail: StateFlow<SecretCapsuleDetail>
        get() = _capsuleDetail

    override fun getSecretCapsuleDetail(id: Long) {
        viewModelScope.launch {
            secretCapsuleDetailUseCase(id).collect{ result ->
                result.onSuccess { detail ->
                    _capsuleDetail.emit(detail)
                }.onFail {

                }
            }
        }
    }
}