package com.droidblossom.archive.presentation.ui.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentMyPageBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<MyPageViewModelImpl, FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override val viewModel: MyPageViewModelImpl by viewModels()

    override fun observeData() {

    }

}