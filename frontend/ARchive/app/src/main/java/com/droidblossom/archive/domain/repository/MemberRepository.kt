package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.data.dto.member.request.MemberDataRequestDto
import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.data.dto.member.request.NotificationEnabledRequestDto
import com.droidblossom.archive.data.dto.member.response.NotificationResponseDto
import com.droidblossom.archive.domain.model.auth.Health
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.model.member.MemberStatus
import com.droidblossom.archive.domain.model.member.NotificationPage
import com.droidblossom.archive.util.RetrofitResult

interface MemberRepository {

    suspend fun getMe() : RetrofitResult<MemberDetail>

    suspend fun patchMe(request: MemberDataRequestDto) : RetrofitResult<String>

    suspend fun postMemberStatus(request : MemberStatusRequestDto) : RetrofitResult<MemberStatus>

    suspend fun patchNotificationEnabled(request: NotificationEnabledRequestDto): RetrofitResult<String>

    suspend fun patchFcmToken(request: FcmTokenRequsetDto): RetrofitResult<String>

    suspend fun getNotifications(request: PagingRequestDto) : RetrofitResult<NotificationPage>

    suspend fun deleteAccount(): RetrofitResult<String>
    suspend fun changePhoneMessageSend(request: VerificationMessageSendRequestDto): RetrofitResult<String>
    suspend fun changePhoneValidMessage(request: VerificationNumberValidRequestDto): RetrofitResult<String>
    suspend fun getText() : RetrofitResult<Health>

}