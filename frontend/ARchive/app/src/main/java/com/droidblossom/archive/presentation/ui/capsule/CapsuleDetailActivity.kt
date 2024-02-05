package com.droidblossom.archive.presentation.ui.capsule

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCapsuleDetailBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CapsuleDetailActivity : BaseActivity<CapsuleDetailViewModelImpl, ActivityCapsuleDetailBinding>(R.layout.activity_capsule_detail) {
    override val viewModel: CapsuleDetailViewModelImpl? by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
    }

    override fun observeData() {

    }

    companion object {
        const val CAPSULE_DETAIL = "capsule_detail"

        fun newIntent(context: Context) =
            Intent(context, CapsuleDetailActivity::class.java)
    }
}