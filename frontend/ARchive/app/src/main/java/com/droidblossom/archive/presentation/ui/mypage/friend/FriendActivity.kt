package com.droidblossom.archive.presentation.ui.mypage.friend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendVPA
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FriendActivity :
    BaseActivity<FriendViewModelImpl, ActivityFriendBinding>(R.layout.activity_friend) {
    override val viewModel: FriendViewModelImpl by viewModels<FriendViewModelImpl>()

    private val friendVPA by lazy {
        FriendVPA(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView(){
        val layoutParams = binding.tab.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.tab.layoutParams = layoutParams

        binding.vp.adapter = friendVPA

        TabLayoutMediator(binding.tab, binding.vp) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.groupList)
                1 -> getString(R.string.friendList)
                else -> null
            }
        }.attach()

        binding.tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0){
                    binding.addT.text = "그룹 추가"
                } else {
                    binding.addT.text = "친구 추가"
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun observeData() {

    }

    companion object {
        const val FRIEND = "friend"

        fun newIntent(context: Context) =
            Intent(context, FriendActivity::class.java)
    }
}