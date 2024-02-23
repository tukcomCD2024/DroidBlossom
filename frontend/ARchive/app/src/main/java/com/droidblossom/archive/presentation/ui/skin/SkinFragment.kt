package com.droidblossom.archive.presentation.ui.skin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.fab.setOnClickListener {
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
                    mySkinRVA.submitList(skins)
                }
            }
        }
    }

    companion object {

        const val TAG = "SKIN"
        fun newIntent() = SkinFragment()
    }

}