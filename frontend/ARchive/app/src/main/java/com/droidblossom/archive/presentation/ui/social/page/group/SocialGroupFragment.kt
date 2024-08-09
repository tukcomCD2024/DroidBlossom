package com.droidblossom.archive.presentation.ui.social.page.group

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
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
import com.droidblossom.archive.databinding.FragmentSocialGroupBinding
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
class SocialGroupFragment : BaseFragment<SocialGroupViewModelImpl, FragmentSocialGroupBinding>(R.layout.fragment_social_group) {

    override val viewModel: SocialGroupViewModelImpl by viewModels<SocialGroupViewModelImpl>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val socialGroupCapsuleRVA by lazy {
        SocialFriendCapsuleRVA(
            { id ->
                activityResultLauncher.launch(
                    CapsuleDetailActivity.newIntent(
                        requireContext(),
                        id,
                        HomeFragment.CapsuleType.GROUP
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.groupCapsules.collect{ groupCapsules ->
                    socialGroupCapsuleRVA.submitList(groupCapsules){
                        if (binding.socialGroupSwipeRefreshLayout.isRefreshing){
                            binding.socialGroupSwipeRefreshLayout.isRefreshing = false
                            binding.socialGroupRV.scrollToPosition(0)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.socialGroupEvents.collect{  event->
                    when (event){
                        is SocialGroupViewModel.SocialGroupEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        is SocialGroupViewModel.SocialGroupEvent.SwipeRefreshLayoutDismissLoading ->{
                            if (binding.socialGroupSwipeRefreshLayout.isRefreshing){
                                binding.socialGroupSwipeRefreshLayout.isRefreshing = false
                            }
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
                //viewModel.deleteCapsule()
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
        binding.socialGroupRV.adapter = socialGroupCapsuleRVA
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.margin)
        binding.socialGroupRV.addItemDecoration(SpaceItemDecoration(spaceBottom = spaceInPixels))
        binding.socialGroupSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestGroupCapsule()
        }
        
        binding.socialGroupRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        binding.socialGroupRV.scrollToPosition(0)
    }


//    @SuppressLint("ClickableViewAccessibility")
//    fun initSearchEdit(){
//        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
//            if (i == EditorInfo.IME_ACTION_DONE) {
//                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
//                    viewModel.searchGroupCapsule()
//                }
//                true
//            }
//            false
//        }
//        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                viewModel.closeSearchGroupCapsule()
//            }
//        }
//        binding.root.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                val focusedView = binding.searchOpenBtn
//                val outRect = Rect()
//                focusedView.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    focusedView.clearFocus()
//                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
//                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
//                }
//            }
//            false
//        }
//
//        binding.socialGroupRV.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                val focusedView = binding.searchOpenBtn
//                val outRect = Rect()
//                focusedView.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    focusedView.clearFocus()
//                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
//                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
//                }
//            }
//            false
//        }
//
//        binding.searchOpenBtnT.setOnClickListener {
//            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
//            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
//        }
//    }
//
//    private fun updateRecyclerViewConstraints(isSearchOpen: Boolean) {
//        val layoutParams = binding.socialGroupRV.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.topToBottom = if (isSearchOpen) binding.searchOpenBtn.id else binding.searchBtn.id
//        val additionalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics).toInt()
//        layoutParams.topMargin = additionalMargin
//        binding.socialGroupRV.layoutParams = layoutParams
//    }

}