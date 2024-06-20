package com.droidblossom.archive.presentation.ui.mypage.friendaccept.page

import android.os.Bundle
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
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter.FriendSendAcceptRVA
import kotlinx.coroutines.launch

class FriendSendAcceptFragment :
    BaseFragment<FriendAcceptViewModelImpl, FragmentAcceptBinding>(R.layout.fragment_accept) {

    override val viewModel: FriendAcceptViewModelImpl by activityViewModels()

    private val friendSendAcceptRVA by lazy {
        FriendSendAcceptRVA { denyFriend ->
            viewModel.deleteFriendSendRequest(denyFriend)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initView()
        initRV()
    }

    private fun initView() {
        binding.swipeRefreshL.isEnabled = false
        binding.listIsEmptyT.text ="보낸 요청이 없습니다."
    }

    private fun initRV() {
        binding.rv.adapter = friendSendAcceptRVA
        binding.rv.setHasFixedSize(true)
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onScrollFriendSendNearBottom()
                    }
                }
            }
        })
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendSendAcceptList.collect { friendSendAccepts ->
                    friendSendAcceptRVA.submitList(friendSendAccepts)

                    if (friendSendAccepts.isEmpty()) {
                        binding.listIsEmptyT.visibility = View.VISIBLE
                    } else {
                        binding.listIsEmptyT.visibility = View.GONE
                    }
                }
            }
        }
    }
}