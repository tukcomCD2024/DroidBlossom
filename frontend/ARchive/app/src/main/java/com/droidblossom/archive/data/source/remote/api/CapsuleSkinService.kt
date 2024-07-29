package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinDeleteResultResponseDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsMakeResponseDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsSearchPageResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSkinSummaryResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CapsuleSkinService {

    @GET("capsule-skins")
    suspend fun getCapsuleSkinsPageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt: String
    ) : Response<ResponseBody<CapsuleSkinsPageResponseDto>>

    @POST("capsule-skins")
    suspend fun postCapsuleSkinsApi(
        @Body request : CapsuleSkinsMakeRequestDto
    ): Response<ResponseBody<CapsuleSkinsMakeResponseDto>>

    @DELETE("capsule-skins/{capsule_skin_id}")
    suspend fun deleteCapsuleSkinsApi(
        @Path("capsule_skin_id") capsuleSkinId : Long,
    ) : Response<ResponseBody<CapsuleSkinDeleteResultResponseDto>>

    @PATCH("capsule-skins/{capsule_skin_id}")
    suspend fun patchCapsuleSkinsModifyApi(
        @Path("capsule_skin_id") capsuleSkinId : Long,
    ) : Response<ResponseBody<String>>

    @GET("capsule-skins/search")
    suspend fun getCapsuleSkinSearchApi(
        @Query("capsule_skin_name") capsule_skin_name : String,
        @Query("size") size : Int,
        @Query("capsule_skin_id") capsule_skin_id : Int
    ) : Response<ResponseBody<CapsuleSkinsSearchPageResponseDto>>
}