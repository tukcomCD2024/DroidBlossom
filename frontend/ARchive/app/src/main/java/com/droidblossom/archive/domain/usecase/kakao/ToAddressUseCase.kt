package com.droidblossom.archive.domain.usecase.kakao

import com.droidblossom.archive.domain.repository.KakaoRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToAddressUseCase @Inject constructor(
    private val repository: KakaoRepository
) {
    suspend operator fun invoke(x: String, y: String) =
        flow<String> {
            runCatching {
                emit(repository.getAddress(x, y))
            }.onFailure {
                it.printStackTrace()
                emit("null")
            }
        }
}