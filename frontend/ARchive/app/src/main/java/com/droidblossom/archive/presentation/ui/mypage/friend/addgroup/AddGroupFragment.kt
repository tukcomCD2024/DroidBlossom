package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentAddGroupBinding
import com.droidblossom.archive.presentation.base.BaseFragment

class AddGroupFragment :
    BaseFragment<AddGroupViewModelImpl, FragmentAddGroupBinding>(R.layout.fragment_add_group) {

    override val viewModel: AddGroupViewModelImpl by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initView()
    }

    private fun initView() {

    }

    override fun observeData() {

    }
}