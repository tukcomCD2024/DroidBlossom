package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentGroupMemberBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.GroupDetailViewModelImpl

class GroupMemberFragment:
    BaseFragment<GroupDetailViewModelImpl, FragmentGroupMemberBinding>(R.layout.fragment_group_member){

    override val viewModel: GroupDetailViewModelImpl by activityViewModels()

    override fun observeData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }

}