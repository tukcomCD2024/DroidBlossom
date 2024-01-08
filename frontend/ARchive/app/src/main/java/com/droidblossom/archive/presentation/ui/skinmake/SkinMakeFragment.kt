package com.droidblossom.archive.presentation.ui.skinmake

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinMakeBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkinMakeFragment : BaseFragment<SkinMakeViewModel, FragmentSkinMakeBinding>(R.layout.fragment_skin_make) {

    lateinit var navController: NavController
    override val viewModel : SkinMakeViewModel by activityViewModels()


    override fun observeData() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView(){


    }

}