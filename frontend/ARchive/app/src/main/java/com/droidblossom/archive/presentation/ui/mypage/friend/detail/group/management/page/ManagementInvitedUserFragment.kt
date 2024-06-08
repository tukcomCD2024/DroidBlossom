package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentManagementInvitedUsersBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter.ManagementInvitedUserRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManagementInvitedUserFragment :
    BaseFragment<ManagementGroupMemberViewModelImpl, FragmentManagementInvitedUsersBinding>(
        R.layout.fragment_management_invited_users
    ) {

    override val viewModel: ManagementGroupMemberViewModelImpl by activityViewModels()

    private val managementInvitedUserRVA by lazy {
        ManagementInvitedUserRVA { position ->
            viewModel.onClickInvitedUser(position)
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.invitedUsers.collect { members ->
                    managementInvitedUserRVA.submitList(members)
                    if (binding.swipeRefreshLayout.isRefreshing){
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.managementGroupMemberEvents.collect {  event ->

                    when(event){
                        ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                        else -> {

                        }
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

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.load()
            }

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
        binding.invitedUsersRV.adapter = managementInvitedUserRVA
        binding.invitedUsersRV.setHasFixedSize(true)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestInvitableFriendList()
            viewModel.getLatestInvitedUserList()
        }

        binding.invitedUsersRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onInvitedUsersRVScrollNearBottom()
                    }
                }
            }
        })
    }

}