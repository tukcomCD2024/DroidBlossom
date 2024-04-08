package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.databinding.FragmentFriendSearchNumberBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter.AddFriendRVA
import com.droidblossom.archive.util.ContactsUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFriendNumberFragment :
    BaseFragment<AddFriendViewModelImpl, FragmentFriendSearchNumberBinding>(com.droidblossom.archive.R.layout.fragment_friend_search_number) {

    override val viewModel: AddFriendViewModelImpl by viewModels()

    lateinit var navController: NavController

    private val addFriendRVA by lazy {
        AddFriendRVA { position ->
            viewModel.checkAddFriendList(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        navController = Navigation.findNavController(view)
        initView()
        permissionCheck()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.recycleView.adapter = addFriendRVA

        binding.closeBtn.setOnClickListener {
            navController.popBackStack()
        }

        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
                    viewModel.searchTag()
                }
                true
            }
            false
        }
        binding.searchOpenEditT.addTextChangedListener {
            viewModel.resetList()
        }

        binding.searchOpenBtnT.setOnClickListener {
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            viewModel.searchTag()
        }

        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.closeSearchNum()
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

        binding.recycleView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = binding.searchOpenBtn
                val outRect = Rect()
                focusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
            false
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

                        is AddFriendViewModel.AddEvent.CloseLoading -> {
                            dismissLoading()
                        }

                        is AddFriendViewModel.AddEvent.OpenLoading -> {
                            showLoading(requireContext())
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addFriendListUI.collect { friends ->
                    addFriendRVA.submitList(friends)
                }
            }
        }
    }

    private fun permissionCheck() {
        val status = ContextCompat.checkSelfPermission(
            requireContext(),
            "android.permission.READ_CONTACTS"
        )
        if (status == PackageManager.PERMISSION_GRANTED) {
            viewModel.contactsSearch(ContactsUtils.getContacts(requireContext()))
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>("android.permission.READ_CONTACTS"),
                100
            )
        }
    }
}