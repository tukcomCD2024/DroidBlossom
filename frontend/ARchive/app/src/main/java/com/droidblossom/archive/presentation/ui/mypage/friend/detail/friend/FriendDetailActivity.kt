package com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendDetailBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.adapter.CapsuleRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendDetailActivity :
    BaseActivity<FriendDetailViewModelImpl, ActivityFriendDetailBinding>(R.layout.activity_friend_detail){

    override val viewModel: FriendDetailViewModelImpl by viewModels<FriendDetailViewModelImpl>()

    private val capsuleRVA: CapsuleRVA by lazy {
        CapsuleRVA(
            { id, type ->

            },
            { capsuleIndex, id, type ->


            }
        )
    }


    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendDetailEvents.collect { event ->
                    when(event){
                        is FriendDetailViewModel.FriendDetailEvent.ShowToastMessage -> {

                        }
                        FriendDetailViewModel.FriendDetailEvent.SwipeRefreshLayoutDismissLoading -> {

                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsules.collect { capsules->
                    capsuleRVA.submitList(capsules)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel


        initView()
        initRV()

        val layoutParams = binding.appBarLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.appBarLayout.layoutParams = layoutParams

    }


    private fun initView(){
        with(binding){
            profileTagT.setOnLongClickListener {
                copyText("tag",vm!!.friendInfo.value.tag)
                true
            }
        }
    }

    private fun initRV(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestCapsuleList()
        }
        binding.rv.adapter = capsuleRVA
        binding.rv.setHasFixedSize(true)
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onScrollNearBottom()
                    }
                }
            }
        })
    }

    companion object {
        private const val FRIEND_ID = "friend_id"
        fun newIntent(context: Context, friendId: Long) =
            Intent(context, FriendDetailActivity::class.java).apply {
                putExtra(FRIEND_ID, friendId)
            }
    }
}