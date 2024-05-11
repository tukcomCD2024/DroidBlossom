package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityAddGroupBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.addgroup.adapter.AddGroupVPA

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
        val layoutParams = binding.appBar.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.appBar.layoutParams = layoutParams

        val layoutParamsToolBar = binding.toolBar.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.toolBar.layoutParams = layoutParamsToolBar

        binding.vp.adapter = addGroupVPA
        binding.vp. currentItem = 0
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val ADD_GROUP = "add_group"

        fun newIntent(context: Context) =
            Intent(context, AddGroupActivity::class.java)
    }
}