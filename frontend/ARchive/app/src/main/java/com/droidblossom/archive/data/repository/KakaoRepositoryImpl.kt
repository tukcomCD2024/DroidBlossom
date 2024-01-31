package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.kakao.response.AddressDto
import com.droidblossom.archive.data.source.remote.api.KakaoService
import com.droidblossom.archive.domain.repository.KakaoRepository
import javax.inject.Inject

class KakaoRepositoryImpl  @Inject constructor(
    private val api: KakaoService
) : KakaoRepository {

    override suspend fun getAddress(x: String, y: String): AddressDto? {
       val response =  api.getAddressApi(x,y)
        return if (response.isSuccessful){
            response.body()
        }else{
            null
        }
    }
}