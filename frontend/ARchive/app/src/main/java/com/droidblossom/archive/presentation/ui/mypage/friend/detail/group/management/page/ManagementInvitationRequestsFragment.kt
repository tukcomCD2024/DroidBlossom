package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page

import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentGroupMemberBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.GroupMemberManagementViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementInvitationRequestsFragment : BaseFragment<GroupMemberManagementViewModelImpl, FragmentGroupMemberBinding>(
    R.layout.fragment_group_member) {

    override val viewModel: GroupMemberManagementViewModelImpl by activityViewModels()


    override fun observeData() {

    }

}