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
import com.droidblossom.archive.presentation.ui.home.HomeFragment
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
        const val CAPSULE_ID = "capsule_id"
        const val CAPSULE_TYPE = "capsule_type"

        fun newIntent(context: Context , capsuleId : Long, capsuleType : HomeFragment.CapsuleType) =
            Intent(context, CapsuleDetailActivity::class.java).apply {
                putExtra(CAPSULE_ID, capsuleId)
                putExtra(CAPSULE_TYPE, capsuleType)
            }
    }
}