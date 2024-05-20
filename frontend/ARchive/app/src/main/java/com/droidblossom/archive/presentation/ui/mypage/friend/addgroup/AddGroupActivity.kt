package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityAddGroupBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.adapter.AddGroupVPA
import com.google.android.material.appbar.AppBarLayout

class AddGroupActivity :
    BaseActivity<AddGroupViewModelImpl, ActivityAddGroupBinding>(R.layout.activity_add_group) {
    override val viewModel: AddGroupViewModelImpl by viewModels()

    private val addGroupVPA by lazy {
        AddGroupVPA(this)
    }

    override fun observeData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initView()
    }

    private fun initView() {
        val layoutParams = binding.coordinatorLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.coordinatorLayout.layoutParams = layoutParams

        window.statusBarColor = ContextCompat.getColor(this, R.color.main_bg_1)

        binding.vp.adapter = addGroupVPA
        binding.vp. currentItem = 0
        binding.closeBtn.setOnClickListener {
            finish()
        }

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                viewModel.collapsedAppBar()
            } else {
                viewModel.expandedAppBar()
            }
        })
    }

    companion object {
        const val ADD_GROUP = "add_group"

        fun newIntent(context: Context) =
            Intent(context, AddGroupActivity::class.java)
    }
}