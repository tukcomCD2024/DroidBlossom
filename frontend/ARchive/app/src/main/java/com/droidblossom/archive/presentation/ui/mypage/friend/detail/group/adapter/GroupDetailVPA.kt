package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page.GroupCapsuleFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page.GroupMemberFragment

class GroupDetailVPA(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = listOf(
        GroupCapsuleFragment(),
        GroupMemberFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(position: Int): Fragment?{
        return fragmentList.getOrNull(position)
    }
}