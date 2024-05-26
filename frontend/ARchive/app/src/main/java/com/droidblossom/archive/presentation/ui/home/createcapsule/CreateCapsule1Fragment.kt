package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCreateCapsule1Binding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.GroupSelectRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCapsule1Fragment :
    BaseFragment<CreateCapsuleViewModelImpl, FragmentCreateCapsule1Binding>(R.layout.fragment_create_capsule1) {

    override val viewModel: CreateCapsuleViewModelImpl by activityViewModels()
    lateinit var navController: NavController

    private val groupRVA by lazy {
        GroupSelectRVA { previousPosition, currentPosition ->
            viewModel.changeGroup(previousPosition, currentPosition)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        navController = Navigation.findNavController(view)

        viewModel.getGroupList()
        if (viewModel.groupTypeInt != 1) {
            navController.navigate(R.id.action_createCapsule1Fragment_to_createCapsule2Fragment)
        }
        initRVA()
    }


    private fun initRVA() {
        binding.recycleView.adapter = groupRVA
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onScrollGroupNearBottom()
                    }
                }
            }
        })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.create1Events.collect {
                    when (it) {
                        CreateCapsuleViewModel.Create1Event.NavigateTo2 -> {
                            navController.navigate(R.id.action_createCapsule1Fragment_to_createCapsule2Fragment)
                        }

                        is CreateCapsuleViewModel.Create1Event.ShowToastMessage -> {
                            showToastMessage(it.message)
                        }

                        else -> Unit
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groups.collect {
                    groupRVA.submitList(it)
                }
            }
        }
    }
}