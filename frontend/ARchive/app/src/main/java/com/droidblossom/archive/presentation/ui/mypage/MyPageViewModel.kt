package com.droidblossom.archive.presentation.ui.mypage

import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import kotlinx.coroutines.flow.StateFlow

interface MyPageViewModel {
    val myInfo : StateFlow<MemberDetail>

    fun getMe()


}