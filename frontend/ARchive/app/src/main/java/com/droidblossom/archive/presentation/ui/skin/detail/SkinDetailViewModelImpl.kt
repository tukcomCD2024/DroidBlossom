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

    private val _removeSkin = MutableStateFlow(false)
    override val removeSkin: StateFlow<Boolean> get() = _removeSkin


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
                            _removeSkin.value = true
                            skinDetailEvent(SkinDetailViewModel.SkinDetailEvent.ShowToastMessage("스킨 삭제를 성공했습니다."))
                            skinDetailEvent(SkinDetailViewModel.SkinDetailEvent.DeleteSkin)
                        }
                        "FAIL" -> {
                            _removeSkin.value = false
                            skinDetailEvent(SkinDetailViewModel.SkinDetailEvent.ShowToastMessage("사용중인 스킨은 삭제할 수 없습니다."))
                        }
                    }
                }.onFail {
                    if (it == 404){
                        _removeSkin.value = true
                        skinDetailEvent(SkinDetailViewModel.SkinDetailEvent.DeleteSkin)
                    }else{
                        _removeSkin.value = false
                    }

                }
            }
        }
    }
}
