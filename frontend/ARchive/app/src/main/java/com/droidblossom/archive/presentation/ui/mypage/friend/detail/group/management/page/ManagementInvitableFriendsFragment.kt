package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentManagementInvitableFriendsBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter.AddFriendRVA
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter.InvitableFriendRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManagementInvitableFriendsFragment :
    BaseFragment<ManagementGroupMemberViewModelImpl, FragmentManagementInvitableFriendsBinding>(
        R.layout.fragment_management_invitable_friends
    ) {
    override val viewModel: ManagementGroupMemberViewModelImpl by activityViewModels()

    private val invitableFriendRVA by lazy {
        InvitableFriendRVA { position ->
            viewModel.onClickInvitableFriend(position)
        }
    }


    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.invitableFriends.collect { members ->
                    invitableFriendRVA.submitList(members)
                    viewModel.remainingInvites = 30 - viewModel.groupMembers.value.size
                    if (binding.swipeRefreshLayout.isRefreshing){
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.managementGroupMemberEvents.collect { event ->
                    when(event){
                        ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading -> {
                            if (binding.swipeRefreshLayout.isRefreshing){
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }
                        else -> {}
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



        }
    }

    private fun initRV() {
        binding.invitableFriendsRV.adapter = invitableFriendRVA
        binding.invitableFriendsRV.setHasFixedSize(true)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestInvitableFriendList()
            viewModel.getLatestInvitedUserList()
        }

        binding.invitableFriendsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onInvitableFriendsRVNearBottom()
                    }
                }
            }
        })
    }

}