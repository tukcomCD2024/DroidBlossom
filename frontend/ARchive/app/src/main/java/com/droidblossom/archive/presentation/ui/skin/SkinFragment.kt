package com.droidblossom.archive.presentation.ui.skin

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment
import com.droidblossom.archive.presentation.ui.skin.adapter.MySkinRVA
import com.droidblossom.archive.presentation.ui.skin.detail.SkinDetailDialogFragment
import com.droidblossom.archive.presentation.ui.skin.detail.SkinDetailDialogFragment.Companion.DELETE_SKIN
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeActivity
import com.droidblossom.archive.util.updateTopConstraintsForSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SkinFragment : BaseFragment<SkinViewModelImpl, FragmentSkinBinding>(R.layout.fragment_skin) {
    override val viewModel: SkinViewModelImpl by viewModels<SkinViewModelImpl>()

    private val mySkinRVA by lazy {
        MySkinRVA { skin ->
            val existingDialog =
                parentFragmentManager.findFragmentByTag(SkinDetailDialogFragment.TAG) as DialogFragment?
            if (existingDialog == null) {
                val dialog = SkinDetailDialogFragment.newInstance(skin)
                dialog.show(parentFragmentManager, SkinDetailDialogFragment.TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            DELETE_SKIN,
            viewLifecycleOwner
        ) { key, bundle ->
            val skinId = bundle.getLong("skin")
            val skinDeleteState = bundle.getBoolean("isDelete")
            if (skinDeleteState){
                viewModel.deleteSkin(skinId)
            }
        }

        binding.vm = viewModel
        initRVA()
        initSearchEdit()
        binding.createCapsuleLayout.setOnClickListener {
            SkinMakeActivity.goSkinMake(requireContext())
        }

        val layoutParams = binding.viewHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.viewHeaderTitle.layoutParams = layoutParams


    }


    private fun initRVA() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getLatestSkinList()
        }
        binding.skinRV.adapter = mySkinRVA
        binding.skinRV.setHasFixedSize(true)
        binding.skinRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skinEvents.collect { event ->
                    when (event) {
                        is SkinViewModel.SkinEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        SkinViewModel.SkinEvent.ToSkinMake -> {
                            SkinMakeActivity.goSkinMake(requireContext())
                        }

                        SkinViewModel.SkinEvent.SwipeRefreshLayoutDismissLoading -> {
                            if (binding.swipeRefreshLayout.isRefreshing){
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                            binding.skinRV.scrollToPosition(0)
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skins.collect { skins ->
                    mySkinRVA.submitList(skins){
                        if (binding.swipeRefreshLayout.isRefreshing){
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.skinRV.scrollToPosition(0)
                        }
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchOpen.collect {
                    val layoutParams = binding.swipeRefreshLayout.layoutParams as ConstraintLayout.LayoutParams
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.closeSearchSkin()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLatestSkinList()
        viewModel.closeSearchSkin()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEdit(){
        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
                    viewModel.searchSkin()
                }
                true
            }
            false
        }
        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.closeSearchSkin()
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

        binding.skinRV.setOnTouchListener { _, event ->
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

    fun scrollToTop(){
        binding.skinRV.scrollToPosition(0)
    }

    override fun onStop() {
        super.onStop()
        Log.d("생명주기","onResume SKIN")
    }



    companion object {

        const val TAG = "SKIN"
        fun newIntent() = SkinFragment()
    }

}