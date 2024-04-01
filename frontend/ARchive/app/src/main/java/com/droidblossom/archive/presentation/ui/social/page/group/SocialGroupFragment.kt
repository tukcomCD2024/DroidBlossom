package com.droidblossom.archive.presentation.ui.social.page.group

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
import com.droidblossom.archive.presentation.ui.social.SocialViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialGroupFragment : BaseFragment<SocialGroupViewModelImpl, FragmentSocialGroupBinding>(R.layout.fragment_social_group) {

    override val viewModel: SocialGroupViewModelImpl by viewModels<SocialGroupViewModelImpl>()

    override fun observeData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }

}