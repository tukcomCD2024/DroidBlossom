package com.droidblossom.archive.presentation.ui.social.page.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSocialFriendBinding
import com.droidblossom.archive.databinding.FragmentSocialGroupBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.social.page.group.SocialGroupViewModelImpl


class SocialFriendFragment : BaseFragment<SocialFriendViewModelImpl, FragmentSocialFriendBinding>(R.layout.fragment_social_friend) {

    override val viewModel: SocialFriendViewModelImpl by viewModels<SocialFriendViewModelImpl>()

    override fun observeData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }

}