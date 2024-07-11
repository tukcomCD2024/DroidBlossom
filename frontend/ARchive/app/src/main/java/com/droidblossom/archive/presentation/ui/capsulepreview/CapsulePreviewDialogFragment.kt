package com.droidblossom.archive.presentation.ui.capsulepreview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCapsulePreviewDialogBinding
import com.droidblossom.archive.databinding.PopupMenuCapsuleBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import com.droidblossom.archive.presentation.customview.CommonDialogFragment
import com.droidblossom.archive.presentation.customview.ImageWithTitleDialogFragment
import com.droidblossom.archive.presentation.ui.capsule.CapsuleDetailActivity
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.capsulepreview.adapter.GroupCapsuleMemberRVA
import com.droidblossom.archive.util.CapsuleTypeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CapsulePreviewDialogFragment :
    BaseDialogFragment<FragmentCapsulePreviewDialogBinding>(R.layout.fragment_capsule_preview_dialog) {

    private val viewModel: CapsulePreviewDialogViewModelImpl by viewModels()

    val capsuleIndex: Int by lazy {
        arguments?.getString("capsule_index")!!.toInt()
    }


    val capsuleId: Long by lazy {
        arguments?.getString("capsule_id")!!.toLong()
    }

    val capsuleType by lazy {
        arguments?.getString("capsule_type")
            ?.let { CapsuleTypeUtils.stringToEnum(it) }
    }

    private val groupMemberRVA by lazy {
        GroupCapsuleMemberRVA()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            //dialog.window?.setDimAmount(0f)
            dialog.window?.setGravity(Gravity.CENTER)

//            val marginBottomDp = 120f
//            val density = resources.displayMetrics.density
//            val marginBottomPx = (marginBottomDp * density).toInt()
//
//            val params = dialog.window?.attributes
//            params?.y = marginBottomPx
//            dialog.window?.attributes = params
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setCapsuleId(capsuleId)

        when (capsuleType) {
            HomeFragment.CapsuleType.SECRET -> {
                setColor(R.color.purple, R.color.purple_alpha70)
                viewModel.getSecretCapsuleSummary()
                viewModel.setCapsuleTypeImage(
                    R.drawable.ic_secret_marker_24,
                    HomeFragment.CapsuleType.SECRET
                )
            }

            HomeFragment.CapsuleType.PUBLIC -> {
                viewModel.getPublicCapsuleSummary()
                viewModel.setCapsuleTypeImage(
                    R.drawable.ic_public_marker_24,
                    HomeFragment.CapsuleType.PUBLIC
                )
            }

            HomeFragment.CapsuleType.GROUP -> {
                setColor(R.color.main_2, R.color.sky_blue_alpha70)
                viewModel.getGroupCapsuleSummary()
                viewModel.setCapsuleTypeImage(
                    R.drawable.ic_group_marker_24,
                    HomeFragment.CapsuleType.GROUP
                )
            }

            HomeFragment.CapsuleType.TREASURE -> {
                viewModel.getTreasureCapsuleSummary()
                viewModel.setCapsuleTypeImage(
                    R.drawable.ic_treasure_marker,
                    HomeFragment.CapsuleType.TREASURE
                )
            }

            else -> {

            }
        }

        val calledFromCamera = arguments?.getBoolean("called_from_camera") ?: false
        viewModel.setCalledFromCamera(calledFromCamera)

        initObserver()
        initView()
    }

    private fun initView() {
        with(binding) {

            groupMemberRecycleView.adapter = groupMemberRVA

            skinCardView.setOnClickListener {
                if (viewModel.capsuleOpenState.value) {
                    moveCapsuleDetail()
                } else {
                    viewModel.openCapsule()
                }
            }

            capsuleMenuImg.setOnClickListener { view ->
                showPopupMenu(view)
            }

//            closeBtn.setOnClickListener {
//                dismiss()
//            }
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endTime.collect {
                    viewModel.setProgressBar()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.visibleTimeProgressBar.collect { isVisible ->
                    updateSkinCardViewConstraints(isVisible)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsulePreviewDialogEvents.collect { event ->
                    when (event) {
                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }


                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.CapsuleOpenSuccess -> {
                            animateProgressBar()
                        }

                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.MoveCapsuleDetail -> {
                            moveCapsuleDetail()
                        }

                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.DismissCapsulePreviewDialog -> {
                            dismiss()
                        }

                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowTreasureCapsuleResult -> {
                            moveTreasureDialog()
                            showToastMessage("축하합니다. 보물을 획득하셨어요!!")
                            dismiss()
                        }

                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowLoading -> {
                            showLoading(requireContext())
                        }

                        is CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.DismissLoading -> {
                            dismissLoading()
                        }

                        else -> {}
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupCapsuleMembers.collect { groupMembers ->
                    groupMemberRVA.submitList(groupMembers)
                    if (groupMembers.isNotEmpty() && groupMembers.all { it.isOpened }) {
                        if (viewModel.timeCapsule.value){
                            viewModel.setGroupCapsuleOpenState()
                        }
                    }
                }
            }
        }
    }

    private fun updateSkinCardViewConstraints(isVisible: Boolean) {
        val constraintLayout = binding.capsuleSkinLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        if (isVisible) {
            constraintSet.connect(
                binding.skinCardView.id, ConstraintSet.TOP,
                binding.progressBar.id, ConstraintSet.TOP,
                0
            )

            constraintSet.connect(
                binding.skinCardView.id, ConstraintSet.BOTTOM,
                binding.progressBar.id, ConstraintSet.BOTTOM,
                0
            )
        } else {
            constraintSet.connect(
                binding.skinCardView.id, ConstraintSet.TOP,
                binding.openProgressBar.id, ConstraintSet.TOP,
                0
            )

            constraintSet.connect(
                binding.skinCardView.id, ConstraintSet.BOTTOM,
                binding.openProgressBar.id, ConstraintSet.BOTTOM,
                0
            )
        }

        constraintSet.applyTo(constraintLayout)
    }

    private fun moveCapsuleDetail() {
        val intent =
            CapsuleDetailActivity.newIntent(requireContext(), capsuleId.toLong(), capsuleType!!)
        startActivity(intent)
    }

    private fun moveTreasureDialog() {
        val sheet = ImageWithTitleDialogFragment.newIntent(
            "보물을 발견했습니다!",
            viewModel.treasureOpenResult.value
        ) { }
        sheet.show(parentFragmentManager, "logoutDialog")
    }


    private fun animateProgressBar() {
        viewModel.capsulePreviewDialogEvent(
            CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowToastMessage(
                "캡슐이 열리는 중입니다."
            )
        )
        val animator = ObjectAnimator.ofInt(binding.openProgressBar, "progress", 0, 100).apply {
            duration = 2000 // 2초 동안
            interpolator = LinearInterpolator() // 여기에 LinearInterpolator 적용
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (capsuleType == HomeFragment.CapsuleType.TREASURE) {
                        viewModel.capsulePreviewDialogEvent(
                            CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowTreasureCapsuleResult
                        )
                    } else {
                        viewModel.capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.MoveCapsuleDetail)
                    }
                    viewModel.setVisibleOpenProgressBar(false)
                }
            })
        }
        animator.start()
    }

    private fun showPopupMenu(view: View) {

        val popupMenuBinding =
            PopupMenuCapsuleBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        val density = requireContext().resources.displayMetrics.density
        val widthPixels = (120 * density).toInt()

        val popupWindow = PopupWindow(
            popupMenuBinding.root,
            widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        if (viewModel.capsuleSummaryResponse.value.isOwner) {
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
                viewModel.deleteCapsule()
            }
            sheet.show(parentFragmentManager, "deleteCapsuleDialog")
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val capsuleState = Bundle().apply {
            putInt("capsuleIndex", capsuleIndex)
            putLong("capsuleId", capsuleId.toLong())
            putBoolean("isOpened", viewModel.capsuleOpenState.value)
        }

        val removeCapsule = Bundle().apply {
            putInt("capsuleIndex", capsuleIndex)
            putLong("capsuleId", capsuleId.toLong())
            putBoolean("remove", viewModel.removeCapsule.value)
        }



        setFragmentResult("capsuleState", capsuleState)
        setFragmentResult(DELETE_CAPSULE, removeCapsule)

    }

    private fun setColor(progressBarColor: Int, titleColor: Int) {
        binding.progressBar.progressTintList =
            ColorStateList.valueOf(requireContext().getColor(progressBarColor))
        binding.openProgressBar.progressBackgroundTintList =
            ColorStateList.valueOf(requireContext().getColor(progressBarColor))
        binding.capsuleDetailLayout.backgroundTintList =
            ColorStateList.valueOf(requireContext().getColor(titleColor))
    }

    companion object {

        const val TAG = "CapsulePreView"
        const val DELETE_CAPSULE = "removeCapsule"
        fun newInstance(
            capsuleIndex: String,
            capsuleId: String,
            capsuleType: String,
            calledFromCamera: Boolean
        ): CapsulePreviewDialogFragment {
            val args = Bundle().apply {
                putString("capsule_index", capsuleIndex)
                putString("capsule_id", capsuleId)
                putString("capsule_type", capsuleType)
                putBoolean("called_from_camera", calledFromCamera)
            }
            return CapsulePreviewDialogFragment().apply {
                arguments = args
            }
        }
    }
}