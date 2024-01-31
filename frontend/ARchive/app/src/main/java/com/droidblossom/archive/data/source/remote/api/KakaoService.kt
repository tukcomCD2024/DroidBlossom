package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.kakao.response.AddressDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoService {

    @Headers("")
    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddressApi(
        @Query("x") x: String,
        @Query("y") y: String,
    ):Response<AddressDto>
}