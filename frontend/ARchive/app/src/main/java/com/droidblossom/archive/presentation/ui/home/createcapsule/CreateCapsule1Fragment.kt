package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCreateCapsule1Binding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCapsule1Fragment :
    BaseFragment<CreateCapsuleViewModelImpl, FragmentCreateCapsule1Binding>(R.layout.fragment_create_capsule1) {

    override val viewModel: CreateCapsuleViewModelImpl by activityViewModels()
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)

        if (!viewModel.isGroupCapsuleCreate){
            navController.navigate(R.id.action_createCapsule1Fragment_to_createCapsule2Fragment )
        }
    }
    override fun observeData() {}
}