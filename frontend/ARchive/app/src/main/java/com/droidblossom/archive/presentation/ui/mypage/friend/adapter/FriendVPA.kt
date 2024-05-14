package com.droidblossom.archive.presentation.ui.mypage.friend.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidblossom.archive.presentation.ui.mypage.friend.page.FriendListFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.page.GroupListFragment

class FriendVPA(activity: FragmentActivity) : FragmentStateAdapter(activity){

    private val fragmentList = listOf(
        GroupListFragment(),
        FriendListFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}