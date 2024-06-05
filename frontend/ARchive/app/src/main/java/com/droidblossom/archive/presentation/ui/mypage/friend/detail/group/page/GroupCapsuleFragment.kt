package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentGroupCapsuleBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.adapter.CapsuleRVA
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.GroupDetailViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.GroupDetailViewModelImpl
import kotlinx.coroutines.launch

class GroupCapsuleFragment :
    BaseFragment<GroupDetailViewModelImpl, FragmentGroupCapsuleBinding>(R.layout.fragment_group_capsule) {

    override val viewModel: GroupDetailViewModelImpl by activityViewModels()

    private val capsuleRVA: CapsuleRVA by lazy {
        CapsuleRVA(
            { id, type ->

            },
            { capsuleIndex, id, type ->


            }
        )
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupDetailEvents.collect { event ->
                    when (event) {
                        GroupDetailViewModel.GroupDetailEvent.SwipeRefreshLayoutDismissLoading -> {

                        }

                        is GroupDetailViewModel.GroupDetailEvent.ShowToastMessage -> {

                        }

                        else -> {

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsules.collect { capsules ->
                    capsuleRVA.submitList(capsules)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRV()

    }

    private fun initRV(){

        binding.groupCapsuleRV.adapter = capsuleRVA
        binding.groupCapsuleRV.setHasFixedSize(true)
        binding.groupCapsuleRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onCapsuleScrollNearBottom()
                    }
                }
            }
        })
    }


}