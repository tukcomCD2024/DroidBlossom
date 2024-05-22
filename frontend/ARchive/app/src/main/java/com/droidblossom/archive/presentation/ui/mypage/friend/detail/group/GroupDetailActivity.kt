package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityGroupDetailBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend.FriendDetailActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend.FriendDetailViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.adapter.GroupDetailVPA
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page.GroupCapsuleFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page.GroupMemberFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupDetailActivity :
    BaseActivity<GroupDetailViewModelImpl, ActivityGroupDetailBinding>(R.layout.activity_group_detail) {

    override val viewModel: GroupDetailViewModelImpl by viewModels<GroupDetailViewModelImpl>()

    var currentPage = 0

    private val groupId: Long by lazy {
        intent.getLongExtra(GROUP_ID, -1)
    }

    private val groupVPA by lazy {
        GroupDetailVPA(this)
    }

    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupDetailEvents.collect { event ->
                    when(event){
                        is GroupDetailViewModel.GroupDetailEvent.ShowToastMessage -> {

                        }
                        GroupDetailViewModel.GroupDetailEvent.SwipeRefreshLayoutDismissLoading -> {
                            if (binding.swipeRefreshLayout.isRefreshing){
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        val layoutParams = binding.appBarLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.appBarLayout.layoutParams = layoutParams
        initView()
        initTab()
        viewModel.setGroupId(groupId)
    }

    private fun initView(){
        with(binding){
            swipeRefreshLayout.setOnChildScrollUpCallback { _, _ ->
                val fragment = groupVPA.getFragment(vp.currentItem)
                when (fragment) {
                    is GroupCapsuleFragment -> {
                        val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupCapsuleRV)
                        recyclerView?.let { !recyclerView.canScrollVertically(-1) } ?: false
                    }
                    is GroupMemberFragment -> {
                        val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupMemberRV)
                        recyclerView?.let { !recyclerView.canScrollVertically(-1) } ?: false
                    }
                    else -> false
                }
            }

            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.getGroupDetail()
            }

            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handleScrollListenersForFragment(position)
                }
            })
        }
    }

    private fun handleScrollListenersForFragment(position: Int) {
        val fragment = groupVPA.getFragment(position)
        when (fragment) {
            is GroupCapsuleFragment -> {
                val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupCapsuleRV)
                setupRecyclerViewScrollListener(recyclerView)
            }
            is GroupMemberFragment -> {
                val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupMemberRV)
                setupRecyclerViewScrollListener(recyclerView)
            }
        }
    }

    private fun setupRecyclerViewScrollListener(recyclerView: RecyclerView?) {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.swipeRefreshLayout.isEnabled = !recyclerView.canScrollVertically(-1)
            }
        })

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (recyclerView != null) {
                binding.swipeRefreshLayout.isEnabled = verticalOffset == 0
            }
        })
    }

    private fun initTab(){
        with(binding){

            vp.adapter = groupVPA

            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                    Log.d("페이지",currentPage.toString())
                }
            })

            TabLayoutMediator(tabLayout, vp) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.groupCapsule)
                    1 -> getString(R.string.groupMember)
                    else -> null
                }
            }.attach()


        }
    }

    companion object {
        private const val GROUP_ID = "group_id"
        fun newIntent(context: Context, groupId: Long) =
            Intent(context, GroupDetailActivity::class.java).apply {
                putExtra(GROUP_ID, groupId)
            }
    }
}