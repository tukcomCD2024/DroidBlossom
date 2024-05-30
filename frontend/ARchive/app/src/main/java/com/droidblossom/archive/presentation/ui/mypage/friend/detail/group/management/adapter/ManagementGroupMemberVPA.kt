package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page.ManagementGroupMembersFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page.ManagementInvitableFriendsFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.page.ManagementInvitedUserFragment

class ManagementGroupMemberVPA(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = listOf(
        ManagementGroupMembersFragment(),
        ManagementInvitableFriendsFragment(),
        ManagementInvitedUserFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(position: Int): Fragment?{
        return fragmentList.getOrNull(position)
    }
}