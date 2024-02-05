package com.droidblossom.archive.presentation.ui.mypage

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentMyPageBinding
import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.SkinRVA
import com.droidblossom.archive.presentation.ui.mypage.adapter.MyCapsuleRVA
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import com.droidblossom.archive.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<MyPageViewModelImpl, FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override val viewModel: MyPageViewModelImpl by viewModels()

    private val myCapsuleRVA by lazy {
        MyCapsuleRVA(DateUtils.dataServerString)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getMe()
        viewModel.getSecretCapsulePage()
        initRVA()
    }

    private fun initRVA() {
        binding.capsuleRecycleView.adapter = myCapsuleRVA
        binding.capsuleRecycleView.animation = null
         //무한 스크롤
        binding.capsuleRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalItemViewCount = recyclerView.adapter!!.itemCount - 1

                if (newState == 2 && !recyclerView.canScrollVertically(1)
                    && lastVisibleItemPosition == totalItemViewCount
                ) {
                    viewModel.getSecretCapsulePage()
                }
            }

        })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.myCapsules.collect{ capsule ->
                    myCapsuleRVA.submitList(capsule)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.myPageEvents.collect{ event ->
                    when(event){
                        is MyPageViewModel.MyPageEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        else ->{}
                    }
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            viewModel.clearCapsules()
        }
    }
    companion object {

        const val TAG = "MY"
        fun newIntent() = MyPageFragment()
    }
}