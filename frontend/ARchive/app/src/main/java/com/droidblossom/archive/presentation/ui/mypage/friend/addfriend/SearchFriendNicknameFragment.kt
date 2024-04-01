package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.droidblossom.archive.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFriendNicknameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friend_search_nickname, container, false)
    }
}