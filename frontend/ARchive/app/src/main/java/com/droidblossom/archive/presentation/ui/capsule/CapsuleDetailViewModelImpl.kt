package com.droidblossom.archive.presentation.ui.capsule

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.usecase.open.PublicCapsuleDetailUseCase
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleDetailUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsuleDetailViewModelImpl @Inject constructor(
    private val secretCapsuleDetailUseCase: SecretCapsuleDetailUseCase,
    private val publicCapsuleDetailUseCase: PublicCapsuleDetailUseCase
): BaseViewModel(), CapsuleDetailViewModel {

    private val _detailEvent = MutableSharedFlow<CapsuleDetailViewModel.DetailEvent>()
    override val detailEvents: SharedFlow<CapsuleDetailViewModel.DetailEvent>
        get() =  _detailEvent.asSharedFlow()

    private val _capsuleDetail = MutableStateFlow(CapsuleDetail())
    override val capsuleDetail: StateFlow<CapsuleDetail>
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

    override fun getPublicCapsuleDetail(id: Long) {
        viewModelScope.launch {
            publicCapsuleDetailUseCase(id).collect{ result ->
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