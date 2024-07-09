package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.data.dto.auth.response.HealthResponseDto
import com.droidblossom.archive.data.dto.auth.response.TokenResponseDto
import com.droidblossom.archive.data.dto.auth.response.VerificationMessageResponseDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.data.dto.member.request.MemberDataRequestDto
import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.data.dto.member.request.NotificationEnabledRequestDto
import com.droidblossom.archive.data.dto.member.request.TagSearchRequestDto
import com.droidblossom.archive.data.dto.member.response.MemberDetailResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberStatusResponseDto
import com.droidblossom.archive.data.dto.member.response.NotificationResponseDto
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.domain.model.auth.Health
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.model.member.MemberStatus
import com.droidblossom.archive.domain.model.member.NotificationPage
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val api: MemberService
) : MemberRepository {
    override suspend fun getMe(): RetrofitResult<MemberDetail> {
        return apiHandler({ api.getMeApi() }) { response: ResponseBody<MemberDetailResponseDto> -> response.result.toModel() }
    }

    override suspend fun patchMe(request: MemberDataRequestDto): RetrofitResult<String> {
        return apiHandler({ api.patchMeApi(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun postMemberStatus(request: MemberStatusRequestDto): RetrofitResult<MemberStatus> {
        return apiHandler({ api.postMeStatusApi(request) }) { response: ResponseBody<MemberStatusResponseDto> -> response.result.toModel() }
    }

    override suspend fun patchNotificationEnabled(request: NotificationEnabledRequestDto): RetrofitResult<String> {
        return apiHandler({ api.patchNotificationEnabled(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun patchFcmToken(request: FcmTokenRequsetDto): RetrofitResult<String> {
        return apiHandler({ api.patchFcmToken(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getNotifications(
        request: PagingRequestDto
    ): RetrofitResult<NotificationPage> {
        return apiHandler({
            api.getNotifications(
                size = request.size,
                createdAt = request.createdAt
            )
        }) { response: ResponseBody<NotificationResponseDto> -> response.result.toModel() }
    }

    override suspend fun deleteAccount(): RetrofitResult<String> {
        return apiHandler({ api.deleteAccountApi() }) { response: ResponseBody<String> -> response.result }
    }

    override suspend fun changePhoneMessageSend(request: VerificationMessageSendRequestDto): RetrofitResult<String>{
        return apiHandler({ api.postChangePhoneSendMessageApi(request) }) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun changePhoneValidMessage(request: VerificationNumberValidRequestDto): RetrofitResult<String> {
        return apiHandler({ api.postChangePhoneValidMessageApi(request) }) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun changeTagSearchAvailable(request: TagSearchRequestDto): RetrofitResult<String> {
        return apiHandler({api.patchTagSearchApi(request)}) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun getText(): RetrofitResult<Health> {
        return apiHandler({ api.getTextApi() }) { response: ResponseBody<HealthResponseDto> -> response.result.toModel() }
    }

}