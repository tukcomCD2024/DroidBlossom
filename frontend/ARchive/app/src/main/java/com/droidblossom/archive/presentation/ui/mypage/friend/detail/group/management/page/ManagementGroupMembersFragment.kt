package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentManagementGroupMembersBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter.ManagementGroupMemberRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManagementGroupMembersFragment :
    BaseFragment<ManagementGroupMemberViewModelImpl, FragmentManagementGroupMembersBinding>(
        R.layout.fragment_management_group_members
    ) {

    override val viewModel: ManagementGroupMemberViewModelImpl by activityViewModels()

    private val managementGroupMemberRVA by lazy {
        ManagementGroupMemberRVA(
            kickGroupMember = { member ->
                viewModel.kickGroupMember(member)
            }
        )
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupMembers.collect { members ->
                    viewModel.remainingInvites = 30 - viewModel.groupMembers.value.size
                    managementGroupMemberRVA.submitList(members.filter { !it.isOwner })
                    if (binding.swipeRefreshLayout.isRefreshing){
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
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
        binding.groupMembersRV.adapter = managementGroupMemberRVA
        binding.groupMembersRV.setHasFixedSize(true)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.load()
        }
    }
}