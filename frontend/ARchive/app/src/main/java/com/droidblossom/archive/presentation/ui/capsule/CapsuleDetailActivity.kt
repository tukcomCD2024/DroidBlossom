package com.droidblossom.archive.presentation.ui.capsule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCapsuleDetailBinding
import com.droidblossom.archive.databinding.PopupMenuCapsuleBinding
import com.droidblossom.archive.domain.model.common.ContentType
import com.droidblossom.archive.domain.model.common.ContentUrl
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
        ImageUrlRVA({ position, list ->
            startActivity(
                ImagesActivity.newIntent(
                    this,
                    list.map { it.url }.toTypedArray(),
                    position
                )
            )
        }, { url ->
            startActivity(
                VideoActivity.newIntent(this, url)
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        initDetail()
        initRVA()
    }

    private fun initDetail() {
        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        val type = intent.intentSerializable(CAPSULE_TYPE, HomeFragment.CapsuleType::class.java)
        val capsuleInd = intent.getLongExtra(CAPSULE_ID, 0)

        when (type) {
            HomeFragment.CapsuleType.SECRET -> {
                viewModel.getSecretCapsuleDetail(capsuleInd)
            }

            HomeFragment.CapsuleType.GROUP -> {

            }

            HomeFragment.CapsuleType.PUBLIC -> {
                viewModel.getPublicCapsuleDetail(capsuleInd)
            }

            null -> {}
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
        binding.capsuleMenuImg.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun initRVA() {
        binding.postImgVP.adapter = imageVP
        binding.postImgVP.offscreenPageLimit = 3
        binding.indicator.attachTo(binding.postImgVP)

    }

    private fun showPopupMenu(view: View) {
        val popupMenuBinding = PopupMenuCapsuleBinding.inflate(LayoutInflater.from(this@CapsuleDetailActivity), null, false)

        val density = this.resources.displayMetrics.density
        val widthPixels = (120 * density).toInt()

        val popupWindow = PopupWindow(
            popupMenuBinding.root,
            widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        // 메뉴 클릭 리스너 설정
        popupMenuBinding.menuMap.setOnClickListener {
            popupWindow.dismiss()
        }
        popupMenuBinding.menuModify.setOnClickListener {
            popupWindow.dismiss()
        }
        popupMenuBinding.menuDelete.setOnClickListener {
            popupWindow.dismiss()
        }

        view.post {

            popupWindow.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = popupWindow.contentView.measuredWidth
            //val popupHeight = popupWindow.contentView.measuredHeight

            val xOff = -(popupWidth + popupWidth/2 + view.width)
            val yOff = -view.height

            popupWindow.showAsDropDown(view, xOff, yOff)

        }
    }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleDetail.collect {
                    imageVP.submitList(
                        (it.imageUrls?.map { url ->
                            ContentUrl(url, ContentType.IMAGE)
                        }?.toList() ?: listOf<ContentUrl>())
                                + (it.videoUrls?.map { url ->
                                    ContentUrl(url, ContentType.VIDEO)
                                }?.toList() ?: listOf<ContentUrl>())
                    )

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