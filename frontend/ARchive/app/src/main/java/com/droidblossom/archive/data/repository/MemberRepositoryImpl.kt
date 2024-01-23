package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.response.TemporaryTokenResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberDetailResponseDto
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.data.source.remote.api.MemberService
import com.droidblossom.archive.domain.model.member.MemberDetail
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
}