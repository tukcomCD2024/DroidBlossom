package com.droidblossom.archive.presentation.ui.mypage.friend.page

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentListFriendBinding
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendRVA
import kotlinx.coroutines.launch

class FriendListFragment :
    BaseFragment<FriendViewModelImpl, FragmentListFriendBinding>(R.layout.fragment_list_friend) {

    override val viewModel: FriendViewModelImpl by activityViewModels()

    private val friendRVA by lazy {
        FriendRVA(requireContext()){

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initView()
    }

    private fun initView() {
        binding.friendRV.adapter = friendRVA

        binding.friendRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 3) {
                        viewModel.getFriendList()
                    }
                }
            }
        })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendListUI.collect{ friends ->
                    friendRVA.submitList(friends)
                }
            }
        }
    }

}