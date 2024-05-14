package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.AddGroupFragment

class AddGroupVPA(activity: FragmentActivity) : FragmentStateAdapter(activity){

    private val fragmentList = listOf(
        AddGroupFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}