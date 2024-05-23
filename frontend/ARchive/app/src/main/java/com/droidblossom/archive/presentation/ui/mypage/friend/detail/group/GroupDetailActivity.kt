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
import com.droidblossom.archive.util.AppBarStateChangeListener
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAppBarExpanded.collect {
                    binding.swipeRefreshLayout.isEnabled = it
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
                        recyclerView?.let { recyclerView.canScrollVertically(-1) } ?: true
                    }
                    is GroupMemberFragment -> {
                        val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupMemberRV)
                        recyclerView?.let { recyclerView.canScrollVertically(-1) } ?: true
                    }
                    else -> true
                }
            }

            binding.swipeRefreshLayout.setOnRefreshListener {
                if (viewModel.isAppBarExpanded.value){
                    viewModel.getGroupDetail()
                }
            }

            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                }
            })

            appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
                override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                    when (state) {
                        State.EXPANDED -> {
                            vm!!.setIsAppBarExpanded(true)
                        }
                        State.COLLAPSED -> {
                            vm!!.setIsAppBarExpanded(false)
                        }
                        State.IDLE -> {
                            vm!!.setIsAppBarExpanded(false)
                        }
                    }
                }

            })
        }
    }


    private fun initTab(){
        with(binding){

            vp.adapter = groupVPA

            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
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