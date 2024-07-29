package com.droidblossom.archive.presentation.ui.skin.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinDeleteUseCase
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
class SkinDetailViewModelImpl @Inject constructor(
    private val capsuleSkinDeleteUseCase: CapsuleSkinDeleteUseCase
): BaseViewModel(), SkinDetailViewModel {
    
    private val _skinDetailEvents = MutableSharedFlow<SkinDetailViewModel.SkinDetailEvent>()
    override val skinDetailEvents: SharedFlow<SkinDetailViewModel.SkinDetailEvent>
        get() = _skinDetailEvents.asSharedFlow()
    
    private val _skin = MutableStateFlow(CapsuleSkinSummary(
        id = 0,
        skinUrl = "",
        name = "",
        createdAt = "",
        isClicked = false
    ))
    override val skin: StateFlow<CapsuleSkinSummary>
        get() = _skin

    override fun skinDetailEvent(event: SkinDetailViewModel.SkinDetailEvent) {
        viewModelScope.launch {
            _skinDetailEvents.emit(event)
        }
    }

    override fun setSkin(skin: CapsuleSkinSummary) {
        _skin.value = skin
    }

    override fun deleteSkin(){
        viewModelScope.launch {
            capsuleSkinDeleteUseCase(skin.value.id).collect{
                it.onSuccess { result ->
                    when(result.capsuleSkinDeleteResult){
                        "SUCCESS" -> {

                        }
                        "FAIL" -> {

                        }
                    }
                    Log.d("캡스", "$result")
                }.onFail {

                }
            }
        }
    }
}
