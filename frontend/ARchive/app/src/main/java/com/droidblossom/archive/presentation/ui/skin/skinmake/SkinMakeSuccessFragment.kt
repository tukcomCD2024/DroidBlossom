package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinMakeSuccessBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SkinMakeSuccessFragment : BaseFragment<SkinMakeViewModelImpl,FragmentSkinMakeSuccessBinding>(R.layout.fragment_skin_make_success) {

    lateinit var navController: NavController
    override val viewModel : SkinMakeViewModelImpl by activityViewModels()

    override fun observeData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.vm = viewModel
        initView()

        lifecycleScope.launch {
            delay(2000)
            activity?.finish()
        }
    }

    private fun initView(){

    }

}