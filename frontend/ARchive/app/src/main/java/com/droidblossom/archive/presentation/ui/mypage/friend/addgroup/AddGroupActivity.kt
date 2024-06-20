package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityAddGroupBinding
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.adapter.AddGroupVPA
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class AddGroupActivity :
    BaseActivity<AddGroupViewModelImpl, ActivityAddGroupBinding>(R.layout.activity_add_group) {
    override val viewModel: AddGroupViewModelImpl by viewModels()

    private val addGroupVPA by lazy {
        AddGroupVPA(this)
    }

    private val picMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.groupProfileUri.value = uri
            } else {
                Log.d("포토", "No Media selected")
            }
        }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addGroupEvent.collect { event ->
                    when (event) {
                        is AddGroupViewModel.AddGroupEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        is AddGroupViewModel.AddGroupEvent.Finish -> {
                            finish()
                        }

                        else -> {}
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.checkedList.collect { friends ->
                    binding.chipGroup.removeAllViews()
                    friends.forEach {
                        initChip(it)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        initView()
        viewModel.getFriendList()
    }

    private fun initView() {
        val layoutParams = binding.coordinatorLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.coordinatorLayout.layoutParams = layoutParams

        window.statusBarColor = ContextCompat.getColor(this, R.color.main_bg_1)

        binding.vp.adapter = addGroupVPA
        binding.vp.currentItem = 0

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                viewModel.collapsedAppBar()
            } else {
                viewModel.expandedAppBar()
            }
        })

        binding.groupProfileCV.setOnClickListener {
            picMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initChip(friend: FriendsSearchResponse) {
        val chip = Chip(this, null, R.style.Widget_MyApp_EntryChip).apply {
            text = friend.nickname
            setTextColor(ContextCompat.getColor(this@AddGroupActivity, R.color.white))
            isCloseIconVisible = true
            setCloseIconTintResource(R.color.white)
            setChipBackgroundColorResource(R.color.main_1)
            setChipStrokeColorResource(R.color.main_bg_1)
            setOnCloseIconClickListener {
                viewModel.onCloseChip(friend)
            }
        }
        binding.chipGroup.addView(chip)
    }

    companion object {
        const val ADD_GROUP = "add_group"

        fun newIntent(context: Context) =
            Intent(context, AddGroupActivity::class.java)
    }
}