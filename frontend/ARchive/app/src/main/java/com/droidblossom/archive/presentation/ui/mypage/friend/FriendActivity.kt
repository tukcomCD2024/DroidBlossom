package com.droidblossom.archive.presentation.ui.mypage.friend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.adapter.FriendVPA
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.AddFriendActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.AddGroupActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FriendActivity :
    BaseActivity<FriendViewModelImpl, ActivityFriendBinding>(R.layout.activity_friend) {
    override val viewModel: FriendViewModelImpl by viewModels<FriendViewModelImpl>()

    private val friendVPA by lazy {
        FriendVPA(this)
    }

    private var createResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_FRIEND_CODE) {
            if (result.data?.getBooleanExtra(REFRESH, false) == true){
                viewModel.setEvent(FriendViewModel.FriendEvent.OnRefresh)
            }
        }
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
            createResultLauncher.launch(AddGroupActivity.newIntent(this@FriendActivity))
        }

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
                        createResultLauncher.launch(AddGroupActivity.newIntent(this@FriendActivity))
                    }
                } else {
                    binding.addT.text = "친구 추가"
                    binding.addCV.setOnClickListener {
                        startActivity(AddFriendActivity.newIntent(this@FriendActivity))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
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
                        is FriendViewModel.FriendEvent.OnRefresh -> {
                            viewModel.getGroupLastList()
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
        const val TYPE_KEY = "type_key"
        const val REFRESH = "need_refresh"
        const val RESULT_FRIEND_CODE = 104
        fun newIntent(context: Context, type: String) =
            Intent(context, FriendActivity::class.java).apply {
                putExtra(TYPE_KEY, type)
            }
    }
}