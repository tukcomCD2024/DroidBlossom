package com.droidblossom.archive.presentation.ui.social.page.group

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSocialGroupBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.social.adapter.SocialFriendCapsuleRVA
import com.droidblossom.archive.presentation.ui.social.adapter.TestSocialFriendModel
import com.droidblossom.archive.util.SpaceItemDecoration
import com.droidblossom.archive.util.updateTopConstraintsForSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SocialGroupFragment : BaseFragment<SocialGroupViewModelImpl, FragmentSocialGroupBinding>(R.layout.fragment_social_group) {

    override val viewModel: SocialGroupViewModelImpl by viewModels<SocialGroupViewModelImpl>()

    private val socialFriendCapsuleRVA by lazy {
        SocialFriendCapsuleRVA()
    }
    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchOpen.collect {
                    val layoutParams = binding.socialGroupRV.layoutParams as ConstraintLayout.LayoutParams
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initSearchEdit()
        initRVA()
    }

    private fun initRVA() {
        binding.socialGroupRV.adapter = socialFriendCapsuleRVA

        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.margin)
        binding.socialGroupRV.addItemDecoration(SpaceItemDecoration(spaceBottom = spaceInPixels))
        socialFriendCapsuleRVA.submitList(dummyGroupCapsules())
    }


    private fun dummyGroupCapsules(): MutableList<TestSocialFriendModel>{

        val dummyList = mutableListOf<TestSocialFriendModel>()

        for(i in 1..10){
            val capsule = TestSocialFriendModel(
                id = i.toLong(),
                capsuleTitle = "comprehensam",
                capsuleWriter = "deseruisse",
                capsuleContent = "eirmod",
                capsuleContentImg = "enim",
                capsuleLocation = "discere",
                capsuleCreateTime = "amet",
                isOpened = i % 2 == 0
            )

            dummyList.add(capsule)
        }

        return dummyList
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEdit(){
        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
                    viewModel.searchGroupCapsule()
                }
                true
            }
            false
        }
        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.closeSearchGroupCapsule()
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

        binding.socialGroupRV.setOnTouchListener { _, event ->
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

    private fun updateRecyclerViewConstraints(isSearchOpen: Boolean) {
        val layoutParams = binding.socialGroupRV.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = if (isSearchOpen) binding.searchOpenBtn.id else binding.searchBtn.id
        val additionalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics).toInt()
        layoutParams.topMargin = additionalMargin
        binding.socialGroupRV.layoutParams = layoutParams
    }


}