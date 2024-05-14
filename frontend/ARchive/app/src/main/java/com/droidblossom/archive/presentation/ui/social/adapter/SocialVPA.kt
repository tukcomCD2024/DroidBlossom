package com.droidblossom.archive.presentation.ui.social.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendFragment
import com.droidblossom.archive.presentation.ui.social.page.group.SocialGroupFragment

class SocialVPA(fragment: Fragment) : FragmentStateAdapter(fragment){

    private val fragmentList = listOf(
        SocialGroupFragment(),
        SocialFriendFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}