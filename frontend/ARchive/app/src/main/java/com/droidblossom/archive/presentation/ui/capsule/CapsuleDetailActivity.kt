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
import com.droidblossom.archive.presentation.customview.CommonDialogFragment
import com.droidblossom.archive.presentation.ui.capsule.adapter.ImageUrlRVA
import com.droidblossom.archive.presentation.ui.capsulepreview.adapter.GroupCapsuleMemberRVA
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.util.CapsuleTypeUtils
import com.droidblossom.archive.util.intentSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CapsuleDetailActivity :
    BaseActivity<CapsuleDetailViewModelImpl, ActivityCapsuleDetailBinding>(R.layout.activity_capsule_detail) {
    override val viewModel: CapsuleDetailViewModelImpl by viewModels()

    private var type: HomeFragment.CapsuleType? = null
    private var capsuleId: Long = 0

    private val groupMemberRVA by lazy {
        GroupCapsuleMemberRVA()
    }

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

        type = intent.intentSerializable(CAPSULE_TYPE, HomeFragment.CapsuleType::class.java)
        capsuleId = intent.getLongExtra(CAPSULE_ID, 0)

        when (type) {
            HomeFragment.CapsuleType.SECRET -> {
                viewModel.getSecretCapsuleDetail(capsuleId)
            }

            HomeFragment.CapsuleType.GROUP -> {
                viewModel.getGroupCapsuleDetail(capsuleId)
            }

            HomeFragment.CapsuleType.PUBLIC -> {
                viewModel.getPublicCapsuleDetail(capsuleId)
            }

            else -> {

            }
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

        binding.groupNumberRecycleView.adapter = groupMemberRVA

    }

    private fun showPopupMenu(view: View) {

        val popupMenuBinding = PopupMenuCapsuleBinding.inflate(
            LayoutInflater.from(this@CapsuleDetailActivity),
            null,
            false
        )

        val density = this.resources.displayMetrics.density
        val widthPixels = (120 * density).toInt()

        val popupWindow = PopupWindow(
            popupMenuBinding.root,
            widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        if (viewModel.capsuleDetail.value.isOwner) {
            popupMenuBinding.menuDelete.visibility = View.VISIBLE
            popupMenuBinding.menuReport.visibility = View.GONE
        } else {
            popupMenuBinding.menuDelete.visibility = View.GONE
            popupMenuBinding.menuReport.visibility = View.VISIBLE
        }

        popupMenuBinding.menuReport.setOnClickListener {
            popupWindow.dismiss()
        }
        popupMenuBinding.menuDelete.setOnClickListener {
            val sheet = CommonDialogFragment.newIntent(
                "캡슐을 삭제하면 모든 데이터가 사라지며, 되돌릴 수 없습니다.",
                "캡슐 삭제"
            ) {
                viewModel.deleteCapsule(capsuleId, CapsuleTypeUtils.enumToString(type!!))
            }
            sheet.show(supportFragmentManager, "deleteCapsuleDialog")
            popupWindow.dismiss()
        }

        view.post {

            popupWindow.contentView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            val popupWidth = popupWindow.contentView.measuredWidth
            //val popupHeight = popupWindow.contentView.measuredHeight

            val xOff = -(popupWidth + view.width)
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
                    groupMemberRVA.submitList(it.members)
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

                        is CapsuleDetailViewModel.DetailEvent.DismissLoading -> {
                            dismissLoading()
                        }

                        is CapsuleDetailViewModel.DetailEvent.ShowLoading -> {
                            showLoading(this@CapsuleDetailActivity)
                        }

                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.removeCapsule.filter { it }.collect{
                    setResult(DELETE_CAPSULE, Intent().apply {
                        putExtra("capsuleIndex", -1)
                        putExtra("capsuleId", capsuleId)
                        putExtra("remove", viewModel.removeCapsule.value)
                    }).also { finish() }
                }
            }
        }
    }

    companion object {
        const val CAPSULE_DETAIL = "capsule_detail"
        const val CAPSULE_INDEX = "capsule_index"
        const val CAPSULE_ID = "capsule_id"
        const val CAPSULE_TYPE = "capsule_type"
        const val DELETE_CAPSULE = 111

        fun newIntent(context: Context, capsuleId: Long, capsuleType: HomeFragment.CapsuleType) =
            Intent(context, CapsuleDetailActivity::class.java).apply {
                putExtra(CAPSULE_ID, capsuleId)
                putExtra(CAPSULE_TYPE, capsuleType)
            }
    }
}