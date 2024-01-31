package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.kakao.response.AddressDto

interface KakaoRepository {

    suspend fun getAddress(x : String, y :String) : AddressDto?

}