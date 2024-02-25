package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
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

    suspend fun postMemberStatus(request : MemberStatusRequestDto) : RetrofitResult<MemberStatus>

    suspend fun patchNotificationEnabled(request: NotificationEnabledRequestDto): RetrofitResult<String>

    suspend fun patchFcmToken(request: FcmTokenRequsetDto): RetrofitResult<String>

    suspend fun getNotifications(size:Int, createdAt: String) : RetrofitResult<NotificationPage>

    suspend fun getText() : RetrofitResult<Health>
}