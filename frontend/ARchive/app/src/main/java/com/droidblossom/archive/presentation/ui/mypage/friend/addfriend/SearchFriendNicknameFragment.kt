package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentFriendSearchNicknameBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFriendNicknameFragment :
    BaseFragment<AddFriendViewModelImpl, FragmentFriendSearchNicknameBinding>(R.layout.fragment_friend_search_nickname) {

    override val viewModel: AddFriendViewModelImpl by viewModels()

    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {

        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.closeBtn.setOnClickListener {
            (activity as AddFriendActivity).finish()
        }

        binding.addCV.setOnClickListener {
            navController.navigate(R.id.action_searchFriendNicknameFragment_to_searchFriendNumberFragment)
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addEvent.collect { event ->
                    when (event) {
                        is AddFriendViewModel.AddEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}