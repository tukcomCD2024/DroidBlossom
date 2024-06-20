package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendAcceptBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter.FriendAcceptVPA
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.page.FriendAcceptFragment
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.page.GroupAcceptFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FriendAcceptActivity :
    BaseActivity<FriendAcceptViewModelImpl, ActivityFriendAcceptBinding>(R.layout.activity_friend_accept) {
    override val viewModel: FriendAcceptViewModelImpl by viewModels<FriendAcceptViewModelImpl>()
    private var currentVPPosition = 0
    private val friendAcceptVPA by lazy {
        FriendAcceptVPA(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        viewModel.getLastedGroupAcceptList()
        viewModel.getLastedFriendAcceptList()
        viewModel.getFriendSendAcceptListPage()
    }

    private fun initView() {
        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.vp.adapter = friendAcceptVPA

        binding.closeBtn.setOnClickListener {
            finish()
        }

        TabLayoutMediator(binding.tab, binding.vp) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.groupAccept)
                1 -> getString(R.string.friendAccept)
                2 -> getString(R.string.friendSendAccept)
                else -> null
            }
        }.attach()

        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentVPPosition = position
                Log.d("생명", "다름")
            }
        })

        intent.getStringExtra(FRIENDACCEPT)?.let { type ->
            when (type) {
                GROUP -> {
                    binding.tab.getTabAt(0)?.select()
                }

                FRIEND -> {
                    binding.tab.getTabAt(1)?.select()
                }

                else -> {
                    binding.tab.getTabAt(0)?.select()
                }

            }
        }
    }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendAcceptEvent.collect { event ->
                    when (event) {
                        is FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        is FriendAcceptViewModel.FriendAcceptEvent.SwipeRefreshLayoutDismissLoading -> {
                            when (val fragment = friendAcceptVPA.getFragment(currentVPPosition)) {
                                is FriendAcceptFragment -> fragment.onEndSwipeRefresh()
                                is GroupAcceptFragment -> fragment.onEndSwipeRefresh()
                                else -> {}
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    companion object {
        const val FRIEND = "friend"
        const val GROUP = "group"
        const val FRIENDACCEPT = "friend_accept"

        fun newIntent(context: Context, type: String) =
            Intent(context, FriendAcceptActivity::class.java).apply {
                putExtra(FRIENDACCEPT, type)
            }
    }
}