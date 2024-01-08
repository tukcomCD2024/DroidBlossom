package com.droidblossom.archive.presentation.ui.skin.skinmake

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

    private val picMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri != null){
            viewModel.imgUri.value = uri
        }else{
            Log.d("포토", "No Media selected")
        }
    }

    override fun observeData() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.vm = viewModel
        initView()
    }

    private fun initView(){

        with(binding){
            skinImageLayout.setOnClickListener {
                picMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            closeBtn.setOnClickListener {
                MainActivity.goMain(requireContext())
            }

        }

    }

}