package com.droidblossom.archive.presentation.ui.skin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkinFragment : BaseFragment<SkinViewModelImpl,FragmentSkinBinding>(R.layout.fragment_skin) {
    override val viewModel: SkinViewModelImpl by viewModels<SkinViewModelImpl>()
    override fun observeData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        binding.fab.setOnClickListener {
            SkinMakeActivity.goSkinMake(requireContext())
        }
    }

    companion object{

        const val TAG = "SKIN"
        fun newIntent()= SkinFragment()
    }

}