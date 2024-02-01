package com.droidblossom.archive.data.repository

import android.util.Log
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
            Log.d("티티","KakaoRepositoryImpl : API Call Success : ${response.code()} ")
            Log.d("티티","KakaoRepositoryImpl : API Call Success : ${response.body()} ")
            response.body()
        }else{
            Log.e("티티","KakaoRepositoryImpl : API Call Failed : ${response.errorBody()?.toString()} ")
            Log.e("티티", "API Call Failed: ${response.errorBody()?.string() ?: "Unknown error"}")

            null
        }
    }
}