package com.droidblossom.archive.data.repository

import android.util.Log
import com.droidblossom.archive.data.dto.kakao.response.AddressDto
import com.droidblossom.archive.data.source.remote.api.KakaoService
import com.droidblossom.archive.domain.repository.KakaoRepository
import javax.inject.Inject
import javax.inject.Named

class KakaoRepositoryImpl  @Inject constructor(
    @Named("kakao") private val api: KakaoService
) : KakaoRepository {

    override suspend fun getAddress(x: String, y: String): String {
       val response =  api.getAddressApi(x,y)
        Log.d("티티","KakaoRepositoryImpl : API Call Success : ${response.code()} ")
        return if (response.isSuccessful){
            response.body()?.documents?.first()?.address?.address_name ?: "없음"
        }else{
            "없음"
        }
    }
}