package com.droidblossom.archive.presentation.ui.home.dialog

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleSummaryUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsulePreviewDialogViewModelImpl @Inject constructor(
    private val secretCapsuleSummaryUseCase: SecretCapsuleSummaryUseCase
) : BaseViewModel(), CapsulePreviewDialogViewModel {

    private val _secretCapsuleSummary = MutableStateFlow(SecretCapsuleSummary("","","","","",false,""))
    override val secretCapsuleSummary: StateFlow<SecretCapsuleSummary>
        get() = _secretCapsuleSummary
    fun getSecretCapsuleSummary(capsuleId : Int){
        viewModelScope.launch {
            secretCapsuleSummaryUseCase(capsuleId).collect{result ->
                result.onSuccess {
                    _secretCapsuleSummary.emit(it)
                    Log.d("getSecretCapsuleSummary","$it")
                }.onFail {

                }.onException {

                }.onError {

                }
            }
        }

    }
    
}