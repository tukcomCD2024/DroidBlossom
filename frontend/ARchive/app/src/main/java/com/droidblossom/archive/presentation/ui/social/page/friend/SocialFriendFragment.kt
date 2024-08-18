package com.droidblossom.archive.presentation.ui.social.page.friend

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSocialFriendBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.capsule.CapsuleDetailActivity
import com.droidblossom.archive.presentation.ui.capsulepreview.CapsulePreviewDialogFragment
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.social.SocialFragment
import com.droidblossom.archive.presentation.ui.social.adapter.SocialFriendCapsuleRVA
import com.droidblossom.archive.util.SpaceItemDecoration
import com.droidblossom.archive.util.updateTopConstraintsForSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SocialFriendFragment : BaseFragment<SocialFriendViewModelImpl, FragmentSocialFriendBinding>(R.layout.fragment_social_friend) {

    override val viewModel: SocialFriendViewModelImpl by viewModels<SocialFriendViewModelImpl>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val socialFriendCapsuleRVA by lazy {
        SocialFriendCapsuleRVA(
            { id ->
                activityResultLauncher.launch(
                    CapsuleDetailActivity.newIntent(
                        requireContext(),
                        id,
                        HomeFragment.CapsuleType.PUBLIC
                    )
                )
                SocialFragment.setReloadFalse()
            },
            {
                showToastMessage("개봉되지 않은 캡슐입니다.")
            }
        )
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.socialFriendEvents.collect { event ->
                    when (event) {
                        is SocialFriendViewModel.SocialFriendEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        is SocialFriendViewModel.SocialFriendEvent.SwipeRefreshLayoutDismissLoading -> {
                            if (binding.socialFriendSwipeRefreshLayout.isRefreshing){
                                binding.socialFriendSwipeRefreshLayout.isRefreshing = false
                            }
                            binding.socialFriendRV.scrollToPosition(0)
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.publicCapsules.collect{ publicCapsules ->
                    socialFriendCapsuleRVA.submitList(publicCapsules){
                        if (binding.socialFriendSwipeRefreshLayout.isRefreshing){
                            binding.socialFriendSwipeRefreshLayout.isRefreshing = false
                            binding.socialFriendRV.scrollToPosition(0)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.socialFriendEvents.collect{  event->
                    when (event){
                        is SocialFriendViewModel.SocialFriendEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        is SocialFriendViewModel.SocialFriendEvent.SwipeRefreshLayoutDismissLoading ->{
                            if (binding.socialFriendSwipeRefreshLayout.isRefreshing){
                                binding.socialFriendSwipeRefreshLayout.isRefreshing = false
                            }
                            binding.socialFriendRV.scrollToPosition(0)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        parentFragmentManager.setFragmentResultListener(
            CapsulePreviewDialogFragment.DELETE_CAPSULE,
            viewLifecycleOwner
        ) { key, bundle ->
            val capsuleIndex = bundle.getInt("capsuleIndex")
            val capsuleId = bundle.getLong("capsuleId")
            val remove = bundle.getBoolean("remove")
            if (remove) {
                viewModel.deleteCapsule(capsuleIndex, capsuleId)
            }
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == CapsuleDetailActivity.DELETE_CAPSULE) {
                val data = result.data
                data?.let {
                    val capsuleIndex = it.getIntExtra("capsuleIndex", -1)
                    val capsuleId = it.getLongExtra("capsuleId", -1L)
                    val remove = it.getBooleanExtra("remove", false)
                    if (remove){
                        viewModel.deleteCapsule(capsuleIndex,capsuleId)
                    }
                }
            }

        }

        initRVA()
    }

    private fun initRVA() {
        binding.socialFriendRV.adapter = socialFriendCapsuleRVA

        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.margin)
        binding.socialFriendRV.addItemDecoration(SpaceItemDecoration(spaceBottom = spaceInPixels))
        binding.socialFriendSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestPublicCapsule()
        }

        binding.socialFriendRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount - lastVisibleItemPosition <= 5) {
                        viewModel.onScrollNearBottom()
                    }
                }
            }
        })
    }

    fun scrollToTop(){
        binding.socialFriendRV.scrollToPosition(0)
    }

    override fun onResume() {
        super.onResume()
        Log.d("소셜", "리쥼")

    }

    override fun onPause() {
        super.onPause()
        Log.d("소셜", "포즈")

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden){
            Log.d("소셜", "히든")
        }else{
            Log.d("소셜", "쇼")
        //
//            if (SocialFragment.getReload()){
//                viewModel.getLatestPublicCapsule()
//            }else{
//                SocialFragment.setReloadTrue()
//            }

        }
    }

}