package com.droidblossom.archive.presentation.ui.social.page.friend

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.social.adapter.SocialFriendCapsuleRVA
import com.droidblossom.archive.util.SpaceItemDecoration
import com.droidblossom.archive.util.updateTopConstraintsForSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SocialFriendFragment : BaseFragment<SocialFriendViewModelImpl, FragmentSocialFriendBinding>(R.layout.fragment_social_friend) {

    override val viewModel: SocialFriendViewModelImpl by viewModels<SocialFriendViewModelImpl>()

    private val socialFriendCapsuleRVA by lazy {
        SocialFriendCapsuleRVA(
            { id ->
                startActivity(
                    CapsuleDetailActivity.newIntent(
                        requireContext(),
                        id,
                        HomeFragment.CapsuleType.PUBLIC
                    )
                )
            },
            {
                showToastMessage("개봉되지 않은 캡슐입니다.")
            }
        )
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchOpen.collect {
                    val layoutParams = binding.socialFriendSwipeRefreshLayout.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.updateTopConstraintsForSearch(
                        isSearchOpen = it,
                        searchOpenView = binding.searchOpenBtn,
                        searchView = binding.searchBtn,
                        additionalMarginDp = 16f,
                        resources = resources
                    )
                    if (it){
                        binding.searchOpenEditT.requestFocus()
                        val imm = requireActivity().getSystemService(InputMethodManager::class.java)
                        imm.showSoftInput(binding.searchOpenEditT, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.socialFriendEvents.collect { event ->
                    when (event) {
                        is SocialFriendViewModel.SocialFriendEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        is SocialFriendViewModel.SocialFriendEvent.HideLoading -> {
                            binding.socialFriendSwipeRefreshLayout.isRefreshing = false
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.publicCapsules.collect{ publicCapsules ->
                    if (viewModel.clearCapsule){
                        viewModel.clearCapsule = false
                    }else{
                        socialFriendCapsuleRVA.submitList(publicCapsules){
                            if (binding.socialFriendSwipeRefreshLayout.isRefreshing){
                                binding.socialFriendSwipeRefreshLayout.isRefreshing = false
                                binding.socialFriendRV.scrollToPosition(0)
                            }
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

                        is SocialFriendViewModel.SocialFriendEvent.HideLoading ->{
                            binding.socialFriendSwipeRefreshLayout.isRefreshing = false
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
        initRVA()
        initSearchEdit()
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
                        viewModel.getPublicCapsulePage()
                    }
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEdit(){
        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
                    viewModel.searchFriendCapsule()
                }
                true
            }
            false
        }
        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.closeSearchFriendCapsule()
            }
        }
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = binding.searchOpenBtn
                val outRect = Rect()
                focusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    focusedView.clearFocus()
                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
            false
        }

        binding.socialFriendRV.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = binding.searchOpenBtn
                val outRect = Rect()
                focusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    focusedView.clearFocus()
                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
            false
        }

        binding.searchOpenBtnT.setOnClickListener {
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }

}