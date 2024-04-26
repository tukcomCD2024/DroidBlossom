package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendAcceptBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendActivity
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter.FriendAcceptVPA
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FriendAcceptActivity :
    BaseActivity<FriendAcceptViewModelImpl, ActivityFriendAcceptBinding>(R.layout.activity_friend_accept) {
    override val viewModel: FriendAcceptViewModelImpl by viewModels<FriendAcceptViewModelImpl>()

    private val friendAcceptVPA by lazy {
        FriendAcceptVPA(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        viewModel.getFriendAcceptList()
    }

    private fun initView(){
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
                else -> null
            }
        }.attach()


        intent.getStringExtra(FriendActivity.TYPE_KEY)?.let { type ->
            when (type) {
                FriendActivity.GROUP -> {
                    binding.tab.getTabAt(0)?.select()
                }

                FriendActivity.FRIEND -> {
                    binding.tab.getTabAt(1)?.select()
                }

                else -> {
                    binding.tab.getTabAt(0)?.select()
                }

            }
        }
    }

    override fun observeData() {

    }

    companion object {
        const val FRIEND = "friend"
        const val GROUP = "group"
        const val FRIENDACCEPT = "friend_accept"

        fun newIntent(context: Context, type : String) =
            Intent(context, FriendAcceptActivity::class.java).apply {
                putExtra(FRIENDACCEPT, type)
            }
    }
}