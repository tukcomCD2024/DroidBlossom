package com.droidblossom.archive.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentHomeBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.snack.HomeSnackBarBig
import com.droidblossom.archive.presentation.snack.HomeSnackBarSmall
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        binding.makeGroupCapsuleBtn.setOnClickListener {
            //그룹 캡슐 생성 페이지로 이동
            startActivity(CreateCapsuleActivity.newIntent(requireContext(),1))
        }
        binding.makeOpenCapsuleBtn.setOnClickListener {
            //공개 캡슐 생성 페이지로 이동
            startActivity(CreateCapsuleActivity.newIntent(requireContext(),2))
        }
        binding.makeSecretCapsuleBtn.setOnClickListener {
            //비밀 캡슐 생성 페이지로 이동
            startActivity(CreateCapsuleActivity.newIntent(requireContext(),3))
        }

        binding.snackbarTestBtn.setOnClickListener {
            //스낵바 스몰 테스트용
            HomeSnackBarSmall(requireView()).show()
        }

        binding.snackbarBigText.setOnClickListener {
            //스낵바 빅 테스트용
            HomeSnackBarBig(requireView(), "", "").show()
        }

    }

    override fun observeData() {
        //FAB 상태
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filterCapsuleSelect.collect(

                )
            }
        }
    }
}