package com.droidblossom.archive.presentation.ui.mypage.friend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendVPA
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.AddFriendActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.AddGroupActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.page.FriendListFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.page.GroupListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FriendActivity :
    BaseActivity<FriendViewModelImpl, ActivityFriendBinding>(R.layout.activity_friend) {
    override val viewModel: FriendViewModelImpl by viewModels<FriendViewModelImpl>()

    var currentPage = 0

    private val friendVPA by lazy {
        FriendVPA(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        viewModel.getFriendList()
        viewModel.getGroupList()
    }

    private fun initView() {
        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.vp.adapter = friendVPA

        binding.addCV.setOnClickListener {
            startActivity(AddGroupActivity.newIntent(this@FriendActivity))
        }

        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        binding.closeBtn.setOnClickListener {
            //startActivity(GroupDetailActivity.newIntent(this@FriendActivity,1))
            //startActivity(FriendDetailActivity.newIntent(this@FriendActivity,1))
            finish()
        }

        TabLayoutMediator(binding.tab, binding.vp) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.groupList)
                1 -> getString(R.string.friendList)
                else -> null
            }
        }.attach()

        binding.tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding.addT.text = "그룹 추가"
                    binding.addCV.setOnClickListener {
                        startActivity(AddGroupActivity.newIntent(this@FriendActivity))
                    }
                } else {
                    binding.addT.text = "친구 추가"
                    binding.addCV.setOnClickListener {
                        startActivity(AddFriendActivity.newIntent(this@FriendActivity))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                scrollToTopCurrentFragment(tab.position)
            }
        })

        intent.getStringExtra(TYPE_KEY)?.let { type ->
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
                viewModel.friendEvent.collect { event ->
                    when (event) {
                        is FriendViewModel.FriendEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        is FriendViewModel.FriendEvent.OnRefreshEnd->{
                            scrollToTopCurrentFragment(currentPage)
                        }
                    }
                }
            }
        }
    }

    fun scrollToTopCurrentFragment(selectedPosition: Int) {
        friendVPA.getFragment(selectedPosition)?.let { fragment ->
            if (fragment.isAdded && fragment.isResumed) {
                when (fragment) {
                    is GroupListFragment -> fragment.onEndSwipeRefresh()
                    is FriendListFragment -> fragment.onEndSwipeRefresh()
                }
            }
        }
    }

    companion object {
        const val FRIEND = "friend"
        const val GROUP = "group"
        const val TYPE_KEY = "type_key"
        fun newIntent(context: Context, type: String) =
            Intent(context, FriendActivity::class.java).apply {
                putExtra(TYPE_KEY, type)
            }
    }
}