package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentManagementInvitedUsersBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementInvitedUserFragment :
    BaseFragment<ManagementGroupMemberViewModelImpl, FragmentManagementInvitedUsersBinding>(
        R.layout.fragment_management_invited_users
    ) {

    override val viewModel: ManagementGroupMemberViewModelImpl by activityViewModels()


    override fun observeData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRV()
        initView()
    }

    private fun initView() {

        with(binding) {

            actionMessage.setOnClickListener {
                viewModel.managementGroupMemberEvent(
                    ManagementGroupMemberViewModel.ManagementGroupMemberEvent.NavigateToPage(
                        ManagementGroupMemberActivity.INVITABLE_FRIENDS
                    )
                )
            }

        }
    }

    private fun initRV() {

    }

}