package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentGroupMemberBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.HomeViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.GroupDetailViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.adapter.GroupMemberRVA
import kotlinx.coroutines.launch

class GroupMemberFragment:
    BaseFragment<GroupDetailViewModelImpl, FragmentGroupMemberBinding>(R.layout.fragment_group_member){

    override val viewModel: GroupDetailViewModelImpl by activityViewModels()

    private val groupMemberRVA by lazy {
        GroupMemberRVA()
    }
    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupMembers.collect { members ->
                    groupMemberRVA.submitList(members)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRV()
    }

    private fun initRV(){
        binding.groupMemberRV.adapter = groupMemberRVA
    }

}