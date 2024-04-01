package com.droidblossom.archive.presentation.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
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
import com.droidblossom.archive.presentation.ui.capsule.CapsuleDetailActivity
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.SkinRVA
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.presentation.ui.mypage.adapter.MyCapsuleRVA
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendActivity
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingActivity
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import com.droidblossom.archive.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<MyPageViewModelImpl, FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override val viewModel: MyPageViewModelImpl by viewModels()

    private val myCapsuleRVA by lazy {
        MyCapsuleRVA(
            { id, type ->
                startActivity(
                    CapsuleDetailActivity.newIntent(
                        requireContext(),
                        id,
                        type
                    )
                )
            },
            { capsuleIndex, id, type ->
                val sheet = CapsulePreviewDialogFragment.newInstance(capsuleIndex.toString(), id.toString(), type.toString(), false)
                sheet.show(parentFragmentManager, "CapsulePreviewDialog")
            },
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getMe()
        viewModel.getSecretCapsulePage()

        parentFragmentManager.setFragmentResultListener("capsuleState", viewLifecycleOwner) { key, bundle ->
            val capsuleIndex = bundle.getInt("capsuleIndex")
            val capsuleId = bundle.getLong("capsuleId")
            val capsuleOpenState = bundle.getBoolean("isOpened")
            if (capsuleIndex != -1 && capsuleOpenState) {
                viewModel.updateCapsuleOpenState(capsuleIndex,capsuleId)
                myCapsuleRVA.notifyItemChanged(capsuleIndex)
            }
        }

        initRVA()

//        binding.settingBtn.setOnClickListener {
//            throw RuntimeException("Test Crash")
//        }

        val layoutParams = binding.profileImg.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.profileImg.layoutParams = layoutParams

        //임시 : 친구 엑티비티로 이동
        binding.profileImg.setOnClickListener{
            startActivity(FriendActivity.newIntent(requireContext()))
        }
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myCapsules.collect { capsule ->
                    if (capsule.isNotEmpty()){
                        viewModel.updateMyCapsulesUI()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myCapsulesUI.collect { capsule ->
                    myCapsuleRVA.submitList(capsule)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myPageEvents.collect { event ->
                    when (event) {
                        is MyPageViewModel.MyPageEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                        is MyPageViewModel.MyPageEvent.ClickSetting -> {
                            startActivity(SettingActivity.newIntent(requireContext()))
                        }

                        is MyPageViewModel.MyPageEvent.CapsuleStateUpdate -> {
                            //myCapsuleRVA.notifyItemChanged(event.capsuleIndex)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.clearCapsules()
        }
    }

    companion object {

        const val TAG = "MY"
        fun newIntent() = MyPageFragment()
    }
}