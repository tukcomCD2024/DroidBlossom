package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.data.dto.auth.response.HealthResponseDto
import com.droidblossom.archive.data.dto.auth.response.TokenResponseDto
import com.droidblossom.archive.data.dto.auth.response.VerificationMessageResponseDto
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.data.dto.member.request.MemberDataRequestDto
import com.droidblossom.archive.data.dto.member.request.MemberDetailUpdateRequestDto
import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.data.dto.member.request.NotificationEnabledRequestDto
import com.droidblossom.archive.data.dto.member.request.PhoneSearchRequestDto
import com.droidblossom.archive.data.dto.member.request.TagSearchRequestDto
import com.droidblossom.archive.data.dto.member.response.AnnouncementsResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberDetailResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberStatusResponseDto
import com.droidblossom.archive.data.dto.member.response.NotificationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberService {

    @GET("me")
    suspend fun getMeApi(): Response<ResponseBody<MemberDetailResponseDto>>

    @POST("me/status")
    suspend fun postMeStatusApi(
        @Body checkStatusRequestDto: MemberStatusRequestDto
    ): Response<ResponseBody<MemberStatusResponseDto>>

    @PATCH("me/data")
    suspend fun patchMeApi(
        @Body request: MemberDataRequestDto
    ): Response<ResponseBody<String>>

    @PATCH("me/notification_enabled")
    suspend fun patchNotificationEnabled(
        @Body request: NotificationEnabledRequestDto
    ): Response<ResponseBody<String>>

    @PATCH("me/fcm_token")
    suspend fun patchFcmToken(
        @Body request: FcmTokenRequsetDto
    ): Response<ResponseBody<String>>

    @GET("me/notifications")
    suspend fun getNotifications(
        @Query("size") size: Int,
        @Query("created_at") createdAt: String
    ): Response<ResponseBody<NotificationResponseDto>>

    @DELETE("me")
    suspend fun deleteAccountApi(): Response<ResponseBody<String>>

    @POST("me/phone/verification/send-message")
    suspend fun postChangePhoneSendMessageApi(
        @Body request: VerificationMessageSendRequestDto
    ): Response<ResponseBody<String>>

    @POST("me/phone/verification/valid-message")
    suspend fun postChangePhoneValidMessageApi(
        @Body request: VerificationNumberValidRequestDto
    ): Response<ResponseBody<String>>

    @PATCH("me/tag-search-available")
    suspend fun patchTagSearchApi(
        @Body request: TagSearchRequestDto
    ): Response<ResponseBody<String>>

    @PATCH("me/phone-search-available")
    suspend fun patchPhoneSearchApi(
        @Body request: PhoneSearchRequestDto
    ): Response<ResponseBody<String>>

    @GET("health")
    suspend fun getTextApi(): Response<ResponseBody<HealthResponseDto>>

    @GET("announcement")
    suspend fun getAnnouncementsApi(): Response<ResponseBody<AnnouncementsResponseDto>>

    @PATCH("me/{target_id}/declaration")
    suspend fun patchUserDeclarationApi(
        @Path("target_id") targetId: Long,
    ): Response<ResponseBody<String>>

}