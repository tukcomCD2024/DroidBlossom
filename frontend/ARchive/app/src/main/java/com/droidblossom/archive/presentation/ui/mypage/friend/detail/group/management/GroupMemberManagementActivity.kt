package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityGroupMemberManagementBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter.ManagementGroupMemberVPA
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupMemberManagementActivity :
    BaseActivity<GroupMemberManagementViewModelImpl, ActivityGroupMemberManagementBinding>(R.layout.activity_group_member_management) {

    var currentPage = 0

    override val viewModel: GroupMemberManagementViewModelImpl by viewModels<GroupMemberManagementViewModelImpl>()

    private val managementGroupMemberVPA by lazy {
        ManagementGroupMemberVPA(this)
    }

    override fun observeData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_member_management)

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
                    Log.d("생명", "다름")
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
            Intent(context, GroupMemberManagementActivity::class.java).apply {

            }
    }
}