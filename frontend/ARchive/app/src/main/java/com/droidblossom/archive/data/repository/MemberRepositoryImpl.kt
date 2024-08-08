package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.data.dto.auth.response.HealthResponseDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.data.dto.member.request.MemberDataRequestDto
import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.data.dto.member.request.NotificationEnabledRequestDto
import com.droidblossom.archive.data.dto.member.request.PhoneSearchRequestDto
import com.droidblossom.archive.data.dto.member.request.TagSearchRequestDto
import com.droidblossom.archive.data.dto.member.response.AnnouncementsResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberDetailResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberStatusResponseDto
import com.droidblossom.archive.data.dto.member.response.NotificationResponseDto
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.data.source.remote.api.UnAuthenticatedService
import com.droidblossom.archive.domain.model.auth.Health
import com.droidblossom.archive.domain.model.member.Announcements
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.model.member.MemberStatus
import com.droidblossom.archive.domain.model.member.NotificationPage
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val unAuthenticatedService: UnAuthenticatedService,
    private val memberService: MemberService
) : MemberRepository {
    override suspend fun getMe(): RetrofitResult<MemberDetail> {
        return apiHandler({ memberService.getMeApi() }) { response: ResponseBody<MemberDetailResponseDto> -> response.result.toModel() }
    }

    override suspend fun patchMe(request: MemberDataRequestDto): RetrofitResult<String> {
        return apiHandler({ memberService.patchMeApi(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun postMemberStatus(request: MemberStatusRequestDto): RetrofitResult<MemberStatus> {
        return apiHandler({ unAuthenticatedService.postMeStatusApi(request) }) { response: ResponseBody<MemberStatusResponseDto> -> response.result.toModel() }
    }

    override suspend fun patchNotificationEnabled(request: NotificationEnabledRequestDto): RetrofitResult<String> {
        return apiHandler({ memberService.patchNotificationEnabled(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun patchFcmToken(request: FcmTokenRequsetDto): RetrofitResult<String> {
        return apiHandler({ memberService.patchFcmToken(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getNotifications(
        request: PagingRequestDto
    ): RetrofitResult<NotificationPage> {
        return apiHandler({
            memberService.getNotifications(
                size = request.size,
                createdAt = request.createdAt
            )
        }) { response: ResponseBody<NotificationResponseDto> -> response.result.toModel() }
    }

    override suspend fun deleteAccount(): RetrofitResult<String> {
        return apiHandler({ memberService.deleteAccountApi() }) { response: ResponseBody<String> -> response.result }
    }

    override suspend fun changePhoneMessageSend(request: VerificationMessageSendRequestDto): RetrofitResult<String>{
        return apiHandler({ memberService.postChangePhoneSendMessageApi(request) }) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun changePhoneValidMessage(request: VerificationNumberValidRequestDto): RetrofitResult<String> {
        return apiHandler({ memberService.postChangePhoneValidMessageApi(request) }) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun changeTagSearchAvailable(request: TagSearchRequestDto): RetrofitResult<String> {
        return apiHandler({memberService.patchTagSearchApi(request)}) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun changePhoneSearchAvailable(request: PhoneSearchRequestDto): RetrofitResult<String> {
        return apiHandler({memberService.patchPhoneSearchApi(request)}) { response : ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun reportUser(userId: Long): RetrofitResult<String> {
        return apiHandler({ memberService.patchUserDeclarationApi(targetId = userId) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getAnnouncements(): RetrofitResult<Announcements> {
        return apiHandler({ memberService.getAnnouncementsApi() }) { response: ResponseBody<AnnouncementsResponseDto> -> response.result.toModel() }
    }

    override suspend fun getServerCheck(): RetrofitResult<Health> {
        return apiHandler({ unAuthenticatedService.getServerCheckApi() }) { response: ResponseBody<HealthResponseDto> -> response.result.toModel() }
    }
}