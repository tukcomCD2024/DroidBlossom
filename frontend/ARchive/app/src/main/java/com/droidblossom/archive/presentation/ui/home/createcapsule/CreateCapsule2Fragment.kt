package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCreateCapsule2Binding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.SkinRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCapsule2Fragment :
    BaseFragment<CreateCapsuleViewModelImpl, FragmentCreateCapsule2Binding>(R.layout.fragment_create_capsule2) {

    override val viewModel: CreateCapsuleViewModelImpl by activityViewModels()
    lateinit var navController: NavController
    private lateinit var callback: OnBackPressedCallback

    private val skinRVA by lazy {
        SkinRVA { viewModel.changeSkin(it) }
    }

    //그룹캡슐이 아닐 경우 바로 엑티비티 닫기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.groupTypeInt != 1) {
                    requireActivity().finish()
                } else {
                    super.remove()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        binding.recycleView.adapter = skinRVA
        binding.recycleView.itemAnimator = null

        Toast.makeText(requireContext(),"${viewModel.capsuleType.value.title}",Toast.LENGTH_SHORT).show()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.create2Events.collect {
                    when (it) {
                        CreateCapsuleViewModel.Create2Event.NavigateTo3 -> {
                            navController.navigate(R.id.action_createCapsule2Fragment_to_createCapsule3Fragment)
                        }

                        CreateCapsuleViewModel.Create2Event.SearchSkin -> {

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skins.collect {
                    skinRVA.submitList(it)
                }
            }
        }
    }

}