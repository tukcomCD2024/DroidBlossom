package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentAddGroupBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter.AddFriendRVA
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeViewModel
import com.droidblossom.archive.util.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddGroupFragment :
    BaseFragment<AddGroupViewModelImpl, FragmentAddGroupBinding>(R.layout.fragment_add_group) {

    override val viewModel: AddGroupViewModelImpl by activityViewModels()

    private val friendRVA by lazy {
        AddFriendRVA { position ->
            viewModel.checkFriendList(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initView()
        initRV()
        initSearchEdit()
    }

    private fun initView() {

        binding.addBtn.setOnClickListener {

            if (viewModel.groupTitle.value.isBlank()) {
                showToastMessage("그룹 이름은 필수입니다.")
                return@setOnClickListener
            }

            if (viewModel.groupContent.value.isBlank()) {
                showToastMessage("그룹 설명은 필수입니다.")
                return@setOnClickListener
            }

            if (viewModel.groupProfileUri.value == null) {
                showToastMessage("그룹 프로필 사진은 필수입니다.")
                return@setOnClickListener
            } else {

                val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentTime = dateFormat.format(Date())

                CoroutineScope(Dispatchers.IO).launch {
                    val groupProfileFile = FileUtils.resizeBitmapFromUri(
                        requireContext(),
                        viewModel.groupProfileUri.value!!,
                        "IMG_${currentTime}"
                    )
                    viewModel.setFile(groupProfileFile!!)
                    viewModel.onCreateGroup()

                }

            }
        }
    }

    private fun initRV() {
        binding.friendRV.adapter = friendRVA
        binding.friendRV.setHasFixedSize(true)

        binding.friendRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisibleItemPosition <= 3) {
                        viewModel.onScrollNearBottomFriend()
                    }
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEdit() {
        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
                    viewModel.search()
                }
                true
            }
            false
        }
        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.closeSearch()
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

        binding.friendRV.setOnTouchListener { _, event ->
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

        binding.searchOpenEditT.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =Unit

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    viewModel.searchFriend()
                }
            }
        })
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendListUI.collect { friends ->
                    friendRVA.submitList(friends)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notifyItemChangedPosition.collect { position ->
                    friendRVA.notifyItemChanged(position)
                }
            }
        }
    }
}