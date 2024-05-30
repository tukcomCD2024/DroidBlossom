package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

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
import com.droidblossom.archive.databinding.ActivityManagementGroupMemberBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter.ManagementGroupMemberVPA
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManagementGroupMemberActivity :
    BaseActivity<ManagementGroupMemberViewModelImpl, ActivityManagementGroupMemberBinding>(R.layout.activity_management_group_member) {

    var currentPage = 0

    override val viewModel: ManagementGroupMemberViewModelImpl by viewModels<ManagementGroupMemberViewModelImpl>()

    private val managementGroupMemberVPA by lazy {
        ManagementGroupMemberVPA(this)
    }

    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.managementGroupMemberEvents.collect { event ->
                    when(event){
                        is ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage -> {

                        }
                        ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading -> {

                        }
                        is ManagementGroupMemberViewModel.ManagementGroupMemberEvent.NavigateToPage -> {
                            binding.groupMemberManagementViewPager.setCurrentItem(event.page,true)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = binding.viewHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.viewHeaderTitle.layoutParams = layoutParams

        initView()

    }

    private fun initView(){

        with(binding){

            groupMemberManagementViewPager.adapter = managementGroupMemberVPA

            groupMemberManagementViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                }
            })

            TabLayoutMediator(groupMemberManagementTab, groupMemberManagementViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "그룹원 목록"
                    1 -> "초대 가능 목록"
                    2 -> "초대 요청 목록"
                    else -> null
                }
            }.attach()
        }

    }

    companion object {

        const val GROUP_MEMBERS = 0
        const val INVITABLE_FRIENDS = 1
        const val INVITATION_REQUESTS = 2

        fun newIntent(context: Context) =
            Intent(context, ManagementGroupMemberActivity::class.java).apply {

            }
    }
}