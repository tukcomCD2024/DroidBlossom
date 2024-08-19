package com.droidblossom.archive.presentation.ui.capsule

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication.Companion.getString
import com.droidblossom.archive.R
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.usecase.capsule.DeleteCapsuleUseCase
import com.droidblossom.archive.domain.usecase.capsule.ReportCapsuleUseCase
import com.droidblossom.archive.domain.usecase.group_capsule.GroupCapsuleDetailUseCase
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
    private val publicCapsuleDetailUseCase: PublicCapsuleDetailUseCase,
    private val groupCapsuleDetailUseCase: GroupCapsuleDetailUseCase,
    private val deleteCapsuleUseCase: DeleteCapsuleUseCase,
    private val reportCapsuleUseCase: ReportCapsuleUseCase,
) : BaseViewModel(), CapsuleDetailViewModel {

    private val _detailEvent = MutableSharedFlow<CapsuleDetailViewModel.DetailEvent>()
    override val detailEvents: SharedFlow<CapsuleDetailViewModel.DetailEvent>
        get() = _detailEvent.asSharedFlow()

    private val _capsuleDetail = MutableStateFlow(CapsuleDetail())
    override val capsuleDetail: StateFlow<CapsuleDetail>
        get() = _capsuleDetail

    private val _removeCapsule = MutableStateFlow(false)
    override val removeCapsule: StateFlow<Boolean> get() = _removeCapsule


    override fun getSecretCapsuleDetail(id: Long) {
        viewModelScope.launch {
            secretCapsuleDetailUseCase(id).collect { result ->
                result.onSuccess { detail ->
                    _capsuleDetail.emit(detail)
                }.onFail {
                    if (it == 404) {
                        _removeCapsule.value = true
                        _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("삭제된 캡슐입니다."))
                        _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.FinishActivity)
                    } else {
                        _detailEvent.emit(
                            CapsuleDetailViewModel.DetailEvent.ShowToastMessage(
                                "캡슐 정보를 불러오는데 실패했습니다. " + getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getPublicCapsuleDetail(id: Long) {
        viewModelScope.launch {
            publicCapsuleDetailUseCase(id).collect { result ->
                result.onSuccess { detail ->
                    _capsuleDetail.emit(detail)
                }.onFail {
                    when (it) {
                        404 -> {
                            _removeCapsule.value = true
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("삭제된 캡슐입니다."))
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.FinishActivity)
                        }

                        403 -> {
                            _removeCapsule.value = true
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("해당 캡슐에 접근 권한이 없습니다."))
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.FinishActivity)
                        }

                        else -> {
                            _detailEvent.emit(
                                CapsuleDetailViewModel.DetailEvent.ShowToastMessage(
                                    "캡슐 정보를 불러오는데 실패했습니다. " + getString(
                                        R.string.reTryMessage
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getGroupCapsuleDetail(id: Long) {
        viewModelScope.launch {
            groupCapsuleDetailUseCase(id).collect { result ->
                result.onSuccess { detail ->
                    _capsuleDetail.emit(detail)
                }.onFail {
                    when (it) {
                        403 -> {
                            _removeCapsule.value = true
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("해당 캡슐에 접근 권한이 없습니다."))
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.FinishActivity)
                        }

                        404 -> {
                            _removeCapsule.value = true
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("삭제된 캡슐입니다."))
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.FinishActivity)
                        }

                        else -> {
                            _detailEvent.emit(
                                CapsuleDetailViewModel.DetailEvent.ShowToastMessage(
                                    "캡슐 정보를 불러오는데 실패했습니다. " + getString(
                                        R.string.reTryMessage
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun deleteCapsule(id: Long, capsuleType: String) {
        viewModelScope.launch {
            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowLoading)
            deleteCapsuleUseCase(capsuleId = id, capsuleType = capsuleType).collect {
                it.onSuccess {
                    _removeCapsule.value = true
                    _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("캡슐 삭제를 성공했습니다."))
                }.onFail {
                    when (it) {
                        403 -> {
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("그룹 캡슐은 그룹장만 삭제할 수 있습니다."))
                        }

                        404 -> {
                            _removeCapsule.value = true
                            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("이미 삭제된 캡슐입니다."))
                        }

                        else -> {
                            _detailEvent.emit(
                                CapsuleDetailViewModel.DetailEvent.ShowToastMessage(
                                    "캡슐 삭제를 실패했습니다. " + getString(
                                        R.string.reTryMessage
                                    )
                                )
                            )
                        }
                    }
                }
            }
            _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.DismissLoading)
        }
    }

    override fun reportCapsule(id: Long) {
        viewModelScope.launch {
            reportCapsuleUseCase(capsuleId = id).collect {
                it.onSuccess {
                    _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.ShowToastMessage("캡슐 신고를 완료했습니다.\n관리자의 검토 후 삭제 조치될 예정입니다."))
                    _detailEvent.emit(CapsuleDetailViewModel.DetailEvent.FinishActivity)
                }.onFail {
                    _detailEvent.emit(
                        CapsuleDetailViewModel.DetailEvent.ShowToastMessage(
                            "캡슐 신고를 실패했습니다. " + getString(
                                R.string.reTryMessage
                            )
                        )
                    )
                }
            }
        }
    }
}