package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.source.remote.api.KakaoService
import com.droidblossom.archive.domain.repository.KakaoRepository
import javax.inject.Inject
import javax.inject.Named

class KakaoRepositoryImpl  @Inject constructor(
    @Named("kakao") private val api: KakaoService
) : KakaoRepository {

    override suspend fun getAddress(x: String, y: String): String {
       val response =  api.getAddressApi(x,y)
        return if (response.isSuccessful){
            response.body()?.documents?.first()?.road_address?.road_name ?: "null"
        }else{
            "null"
        }
    }
}