package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.BuildConfig
import com.droidblossom.archive.data.dto.kakao.response.AddressDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoService {

    @Headers("Authorization: ${BuildConfig.KAKAO_REST_KEY}")
    @GET("v2/local/geo/coord2address.json?query=")
    suspend fun getAddressApi(
        @Query("x") x: String,
        @Query("y") y: String,
    ): Response<AddressDto>
}