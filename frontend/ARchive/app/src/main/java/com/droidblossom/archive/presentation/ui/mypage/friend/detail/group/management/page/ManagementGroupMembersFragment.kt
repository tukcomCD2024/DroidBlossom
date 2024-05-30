package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentManagementGroupMembersBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementGroupMembersFragment :
    BaseFragment<ManagementGroupMemberViewModelImpl, FragmentManagementGroupMembersBinding>(
        R.layout.fragment_management_group_members
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