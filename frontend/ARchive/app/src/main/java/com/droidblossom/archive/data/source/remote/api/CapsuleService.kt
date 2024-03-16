package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleImagesDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleOpenedResponseDto
import com.droidblossom.archive.data.dto.capsule.response.NearbyCapsuleResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface CapsuleService {

    @PATCH("capsules/{capsule_id}/opened")
    suspend fun patchCapsuleOpenApi(
        @Path("capsule_id") capsuleId : Long,
    ) : Response<ResponseBody<CapsuleOpenedResponseDto>>

    @GET("capsules/nearby")
    suspend fun getNearbyCapsulesApi(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("distance") distance : Double,
        @Query("capsule_type") capsule_type : String
    ) : Response<ResponseBody<NearbyCapsuleResponseDto>>

    @GET("map/full-address")
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