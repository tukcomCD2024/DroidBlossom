package com.droidblossom.archive.presentation.ui.mypage.friend.page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentListFriendBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendRVA
import kotlinx.coroutines.launch

class FriendListFragment :
    BaseFragment<FriendViewModelImpl, FragmentListFriendBinding>(R.layout.fragment_list_friend) {

    override val viewModel: FriendViewModelImpl by activityViewModels()

    private val friendRVA by lazy {
        FriendRVA(requireContext(),
            { prev, curr ->
                viewModel.changeDeleteOpen(prev, curr)
            }, { friend ->
                viewModel.deleteFriend(friend)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRV()
    }

    private fun initRV() {
        binding.friendRV.adapter = friendRVA
        binding.friendRV.setHasFixedSize(true)

        binding.swipeRefreshL.setOnRefreshListener {
            viewModel.getLatestFriendList()
        }

        binding.friendRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 3) {
                        viewModel.onScrollNearBottomFriend()
                    }
                }
            }
        })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendListUI.collect { friends ->
                    if (binding.swipeRefreshL.isRefreshing){
                        binding.swipeRefreshL.isRefreshing = false
                        binding.friendRV.scrollToPosition(0)
                    }
                    friendRVA.submitList(friends)
                }
            }
        }
    }

    fun onEndSwipeRefresh() {
        binding.swipeRefreshL.isRefreshing = false
        binding.friendRV.scrollToPosition(0)
    }

}