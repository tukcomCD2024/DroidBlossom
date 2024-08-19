package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCreateCapsuleBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.util.LocationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCapsuleActivity :
    BaseActivity<CreateCapsuleViewModelImpl, ActivityCreateCapsuleBinding>(R.layout.activity_create_capsule) {

    override val viewModel: CreateCapsuleViewModelImpl by viewModels()

    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.createEvents.collect{ event ->
                    when(event){
                        is CreateCapsuleViewModel.CreateEvent.FinishActivity -> {
                            finish()
                        }
                        is CreateCapsuleViewModel.CreateEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.closeBtn.setOnClickListener {
            finish()
        }

        (intent.getIntExtra(CREATE_CAPSULE, 1)).let {
            viewModel.groupTypeInt = it
            viewModel.choseCapsuleType(it)
        }

        val locationUtil = LocationUtil(this)
        locationUtil.getCurrentLocation { latitude, longitude ->
            viewModel.coordToAddress(latitude = latitude, longitude = longitude)
        }
        viewModel.getSkinList()

    }

    companion object {
        const val CREATE_CAPSULE = "create_capsule"

        fun newIntent(context: Context, capsuleType: Int) =
            Intent(context, CreateCapsuleActivity::class.java).apply {
                putExtra(CREATE_CAPSULE, capsuleType)
            }
    }
}