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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home){

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        with(binding){
            vm = viewModel

            makeGroupCapsuleBtn.setOnClickListener {
                startActivity(CreateCapsuleActivity.newIntent(requireContext(), 1))
            }
            makeOpenCapsuleBtn.setOnClickListener {
                startActivity(CreateCapsuleActivity.newIntent(requireContext(), 2))
            }
            makeSecretCapsuleBtn.setOnClickListener {
                startActivity(CreateCapsuleActivity.newIntent(requireContext(), 3))
            }
            snackbarTestBtn.setOnClickListener {
                HomeSnackBarSmall(requireView()).show()
            }
            snackbarBigText.setOnClickListener {
                HomeSnackBarBig(requireView(), "", "").show()
            }
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