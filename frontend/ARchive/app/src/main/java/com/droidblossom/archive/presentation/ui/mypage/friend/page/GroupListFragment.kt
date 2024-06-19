package com.droidblossom.archive.presentation.ui.mypage.friend.page

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentListFriendBinding
import com.droidblossom.archive.databinding.FragmentListGroupBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendRVA
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.GroupRVA
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.GroupDetailActivity
import kotlinx.coroutines.launch

class GroupListFragment :
    BaseFragment<FriendViewModelImpl, FragmentListGroupBinding>(R.layout.fragment_list_group) {

    override val viewModel: FriendViewModelImpl by activityViewModels()

    private val groupDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == GroupDetailActivity.SUCCESS_GROUP_DELETE) {
            val groupId = result.data?.getLongExtra("group_id", -1L)
            groupId?.let {
                if (it != -1L){
                    viewModel.removeGroup(it)
                }
            }
        }
    }

    private val groupAdapter by lazy {
        GroupRVA { groupId ->
            groupDetailLauncher.launch(GroupDetailActivity.newIntent(requireContext(), groupId))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRV()
    }

    private fun initRV() {
        binding.groupRV.adapter = groupAdapter
        binding.groupRV.setHasFixedSize(true)
        binding.groupRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 3) {
                        viewModel.onScrollNearBottomGroup()
                    }
                }
            }
        })

        binding.swipeRefreshL.setOnRefreshListener {
            viewModel.getGroupLastList()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupListUI.collect { groups ->
                    groupAdapter.submitList(groups)
                }
            }
        }
    }

    fun onEndSwipeRefresh() {
        binding.swipeRefreshL.isRefreshing = false
    }
}