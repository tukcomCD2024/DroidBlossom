package com.droidblossom.archive.presentation.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentMyPageBinding
import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.SkinRVA
import com.droidblossom.archive.presentation.ui.mypage.adapter.MyCapsuleRVA
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<MyPageViewModelImpl, FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override val viewModel: MyPageViewModelImpl by viewModels()

    private val myCapsuleRVA by lazy {
        MyCapsuleRVA{  }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getMe()
        initRVA()
    }
    private fun initRVA(){
        binding.capsuleRecycleView.adapter = myCapsuleRVA
    }
    override fun observeData() {

    }

    companion object{

        const val TAG = "MY"
        fun newIntent()= MyPageFragment()
    }
}