package com.droidblossom.archive.presentation.ui.capsule

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.ContentType
import com.droidblossom.archive.domain.model.common.ContentUrl
import com.droidblossom.archive.domain.model.secret.SecretCapsuleDetail
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleDetailUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsuleDetailViewModelImpl @Inject constructor(
    private val secretCapsuleDetailUseCase: SecretCapsuleDetailUseCase
): BaseViewModel(), CapsuleDetailViewModel {

    private val _detailEvent = MutableSharedFlow<CapsuleDetailViewModel.DetailEvent>()
    override val detailEvents: SharedFlow<CapsuleDetailViewModel.DetailEvent>
        get() =  _detailEvent.asSharedFlow()

    private val _capsuleDetail = MutableStateFlow(SecretCapsuleDetail())
    override val capsuleDetail: StateFlow<SecretCapsuleDetail>
        get() = _capsuleDetail

    override fun getSecretCapsuleDetail(id: Long) {
        viewModelScope.launch {
            secretCapsuleDetailUseCase(id).collect{ result ->
                result.onSuccess { detail ->
                    Log.d("디테일","${detail}")
                    _capsuleDetail.emit(detail)
                     }.onFail {
                    Log.d("디테일","${it}")
                    _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("상세정보 불러오기 실패"))
                }
            }
        }
    }
}