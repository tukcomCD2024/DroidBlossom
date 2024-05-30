package com.droidblossom.archive.presentation.ui.mypage.friendaccept.page

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
import com.droidblossom.archive.databinding.FragmentAcceptBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptViewModel
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter.FriendAcceptRVA
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter.GroupAcceptRVA
import kotlinx.coroutines.launch

class GroupAcceptFragment :
    BaseFragment<FriendAcceptViewModelImpl, FragmentAcceptBinding>(R.layout.fragment_accept) {

    override val viewModel: FriendAcceptViewModelImpl by activityViewModels()

    private val groupAcceptRVA by lazy {
        GroupAcceptRVA(
            { denyGroup ->
                viewModel.denyGroupRequest(denyGroup)
            },
            { acceptGroup ->
                viewModel.acceptGroupRequest(acceptGroup)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initView()
        initRV()
    }

    private fun initView() {
        binding.swipeRefreshL.setOnRefreshListener {
            viewModel.getLastedFriendAcceptList()
        }
    }

    private fun initRV() {
        binding.rv.adapter = groupAcceptRVA
        binding.rv.setHasFixedSize(true)
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onScrollGroupNearBottom()
                    }
                }
            }
        })
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupAcceptList.collect { groupAccepts ->
                    groupAcceptRVA.submitList(groupAccepts)

                    if (groupAccepts.isEmpty()) {
                        binding.listIsEmptyT.visibility = View.VISIBLE
                    } else {
                        binding.listIsEmptyT.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun onEndSwipeRefresh() {
        binding.swipeRefreshL.isRefreshing = false
    }
}