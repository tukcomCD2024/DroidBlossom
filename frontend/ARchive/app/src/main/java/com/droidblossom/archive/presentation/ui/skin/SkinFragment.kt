package com.droidblossom.archive.presentation.ui.skin

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.skin.adapter.MySkinRVA
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeActivity
import com.droidblossom.archive.util.LocationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SkinFragment : BaseFragment<SkinViewModelImpl, FragmentSkinBinding>(R.layout.fragment_skin) {
    override val viewModel: SkinViewModelImpl by viewModels<SkinViewModelImpl>()

    private val mySkinRVA by lazy {
        MySkinRVA()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getSkinList()
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
        binding.skinRV.adapter = mySkinRVA
        binding.skinRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalItemViewCount = recyclerView.adapter!!.itemCount - 1

                if (newState == 2 && !recyclerView.canScrollVertically(1)
                    && lastVisibleItemPosition == totalItemViewCount
                ) {
                    viewModel.getSkinList()
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
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skins.collect { skins ->
                    if (skins.isNotEmpty()){
                        viewModel.updateMySkinsUI()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skinsUI.collect { skins ->
                    mySkinRVA.submitList(skins)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchOpen.collect {
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
        if (hidden) {

        } else {
            viewModel.clearSkins()
            viewModel.closeSearchSkin()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearSkins()
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


    companion object {

        const val TAG = "SKIN"
        fun newIntent() = SkinFragment()
    }

}