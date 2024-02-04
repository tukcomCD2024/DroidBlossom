package com.droidblossom.archive.presentation.ui.home.dialog

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.usecase.secret.SecretCapsuleSummaryUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsulePreviewDialogViewModelImpl @Inject constructor(
    private val secretCapsuleSummaryUseCase: SecretCapsuleSummaryUseCase
) : BaseViewModel(), CapsulePreviewDialogViewModel {

    fun getSecretCapsuleSummary(capsuleId : Int){
        viewModelScope.launch {
            secretCapsuleSummaryUseCase(capsuleId).collect{result ->
                result.onSuccess {
                    Log.d("getSecretCapsuleSummary","$it")
                }.onFail {
                    Log.d("getSecretCapsuleSummary","실패")

                }.onException {
                    Log.d("getSecretCapsuleSummary","예외")

                }.onError {
                    Log.d("getSecretCapsuleSummary","에러")

                }
            }
        }

    }
}