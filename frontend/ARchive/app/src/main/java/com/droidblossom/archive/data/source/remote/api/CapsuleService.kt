package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleImagesDto
import com.droidblossom.archive.data.dto.capsule.response.NearbyCapsuleResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CapsuleService {

    @GET("capsules/nearby")
    suspend fun getNearbyCapsulesApi(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("distance") distance : Double,
        @Query("capsule_type") capsule_type : String
    ) : Response<ResponseBody<NearbyCapsuleResponseDto>>

    @GET("capsules/full-address")
    suspend fun getAddressApi(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
    ) : Response<ResponseBody<AddressDataDto>>

    @GET("capsules/images")
    suspend fun getCapsuleImagesApi(
        @Query("size") size : Int,
        @Query("capsule_id") capsuleId : Int,
    ) : Response<ResponseBody<CapsuleImagesDto>>
}