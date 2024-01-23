package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.util.RetrofitResult

interface MemberRepository {

    suspend fun getMe() : RetrofitResult<MemberDetail>
}