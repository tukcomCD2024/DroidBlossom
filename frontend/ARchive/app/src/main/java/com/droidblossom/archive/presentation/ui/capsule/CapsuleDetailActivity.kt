package com.droidblossom.archive.presentation.ui.capsule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCapsuleDetailBinding
import com.droidblossom.archive.domain.model.common.ImageUrl
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.capsule.adapter.ImageUrlRVA
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.util.intentSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CapsuleDetailActivity :
    BaseActivity<CapsuleDetailViewModelImpl, ActivityCapsuleDetailBinding>(R.layout.activity_capsule_detail) {
    override val viewModel: CapsuleDetailViewModelImpl by viewModels()

    private val imageVP by lazy {
        ImageUrlRVA { position , list ->
            startActivity(ImagesActivity.newIntent(this, list.map { it.url }.toTypedArray() , position))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        initDetail()
        initRVA()
    }

    private fun initDetail() {
        val layoutParams = binding.capsuleTypeT.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.capsuleTypeT.layoutParams = layoutParams

        val type = intent.intentSerializable(CAPSULE_TYPE, HomeFragment.CapsuleType::class.java)
        val capsuleInd = intent.getLongExtra(CAPSULE_ID, 0)
        when (type) {
            HomeFragment.CapsuleType.SECRET -> {
                binding.capsuleTypeT.text = type.name
                viewModel.getSecretCapsuleDetail(capsuleInd)
            }

            HomeFragment.CapsuleType.GROUP -> {
                binding.capsuleTypeT.text = type.name

            }

            HomeFragment.CapsuleType.PUBLIC -> {
                binding.capsuleTypeT.text = type.name

            }

            null -> {}
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
        binding.videoCV.setOnClickListener {
            startActivity(
                VideoActivity.newIntent(this, viewModel.capsuleDetail.value.videoUrls!![0])
            )
        }
    }

    private fun initRVA() {
        binding.postImgVP.adapter = imageVP
        binding.postImgVP.offscreenPageLimit = 3
        binding.indicator.attachTo(binding.postImgVP)

    }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleDetail.collect {
                    imageVP.submitList(
                        it.imageUrls?.map { url -> ImageUrl(url) }?.toList() ?: listOf()
                    )
                    if (!it.videoUrls.isNullOrEmpty()) {
                        Glide.with(applicationContext)
                            .load(it.videoUrls.first())
                            .thumbnail(0.1f)
                            .placeholder(R.drawable.app_symbol)
                            .into(binding.videoImg)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailEvents.collect {
                    when (it) {
                        is CapsuleDetailViewModel.DetailEvent.ShowToastMessage -> {
                            showToastMessage(it.message)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    companion object {
        const val CAPSULE_DETAIL = "capsule_detail"
        const val CAPSULE_ID = "capsule_id"
        const val CAPSULE_TYPE = "capsule_type"

        fun newIntent(context: Context, capsuleId: Long, capsuleType: HomeFragment.CapsuleType) =
            Intent(context, CapsuleDetailActivity::class.java).apply {
                putExtra(CAPSULE_ID, capsuleId)
                putExtra(CAPSULE_TYPE, capsuleType)
            }
    }
}