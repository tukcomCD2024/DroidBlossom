package com.droidblossom.archive.presentation.ui.mypage.friendaccept.page

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentAcceptBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptViewModel
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptViewModelImpl
import kotlinx.coroutines.launch

class GroupAcceptFragment :
    BaseFragment<FriendAcceptViewModelImpl, FragmentAcceptBinding>(R.layout.fragment_accept) {

    override val viewModel: FriendAcceptViewModelImpl by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initView()
    }

    private fun initView() {
        binding.swipeRefreshL.setOnRefreshListener {
            viewModel.getLastedFriendAcceptList()
        }
    }

    override fun observeData() {

    }

    fun onEndSwipeRefresh() {
        binding.swipeRefreshL.isRefreshing = false
    }
}