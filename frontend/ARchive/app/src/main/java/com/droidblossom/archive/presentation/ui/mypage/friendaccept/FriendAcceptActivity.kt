package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendAcceptBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendVPA
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FriendAcceptActivity :
    BaseActivity<FriendAcceptViewModelImpl, ActivityFriendAcceptBinding>(R.layout.activity_friend_accept) {
    override val viewModel: FriendAcceptViewModelImpl by viewModels<FriendAcceptViewModelImpl>()

    private val friendVPA by lazy {
        FriendVPA(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    private fun initView(){
        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.vp.adapter = friendVPA

        binding.closeBtn.setOnClickListener {
            finish()
        }

        TabLayoutMediator(binding.tab, binding.vp) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.groupAccept)
                1 -> getString(R.string.friendAccept)
                else -> null
            }
        }.attach()
    }

    override fun observeData() {

    }

    companion object {
        const val FRIENDACCEPT = "friend_accept"

        fun newIntent(context: Context) =
            Intent(context, FriendAcceptActivity::class.java)
    }
}