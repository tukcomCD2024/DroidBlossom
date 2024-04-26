package com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidblossom.archive.presentation.ui.mypage.friend.page.FriendListFragment
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.page.GroupAcceptFragment

class FriendAcceptVPA(activity: FragmentActivity) : FragmentStateAdapter(activity){

    private val fragmentList = listOf(
        GroupAcceptFragment(),
        FriendListFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}