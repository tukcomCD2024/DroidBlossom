package com.droidblossom.archive.presentation.ui.home.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCapsulePreviewDialogBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import com.droidblossom.archive.presentation.ui.capsule.CapsuleDetailActivity
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.util.CapsuleTypeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CapsulePreviewDialogFragment :
    BaseDialogFragment<FragmentCapsulePreviewDialogBinding>(R.layout.fragment_capsule_preview_dialog) {

    private val viewModel: CapsulePreviewDialogViewModelImpl by viewModels()

    val capsuleId: Int by lazy {
        arguments?.getString("capsule_id")!!.toInt()
    }

    val capsuleType by lazy {
        arguments?.getString("capsule_type")
            ?.let { CapsuleTypeUtils.stringToEnum(it) }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setDimAmount(0f)
            dialog.window?.setGravity(Gravity.BOTTOM)

            val marginBottomDp = 120f
            val density = resources.displayMetrics.density
            val marginBottomPx = (marginBottomDp * density).toInt()

            val params = dialog.window?.attributes
            params?.y = marginBottomPx
            dialog.window?.attributes = params
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        when (capsuleType) {
            HomeFragment.CapsuleType.SECRET -> {
                viewModel.getSecretCapsuleSummary(capsuleId)
                viewModel.setCapsuleTypeImage(R.drawable.ic_secret_marker_24)
            }

            HomeFragment.CapsuleType.PUBLIC -> {

            }

            HomeFragment.CapsuleType.GROUP -> {

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
            skinCardView.setOnClickListener {
                if (viewModel.capsuleOpenState.value) {
                    moveCapsuleDetail()
                } else {
                    viewModel.openCapsule(capsuleId.toLong())
                }
            }
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

        viewLifecycleOwner.lifecycleScope.launch{
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

    private fun moveCapsuleDetail(){
        val intent = CapsuleDetailActivity.newIntent(requireContext(), capsuleId.toLong(), capsuleType!!)
        startActivity(intent)
    }


    private fun animateProgressBar() {
        viewModel.capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.ShowToastMessage("캡슐이 열리는 중입니다."))
        val animator = ObjectAnimator.ofInt(binding.openProgressBar, "progress", 0, 100).apply {
            duration = 2000 // 2초 동안
            interpolator = LinearInterpolator() // 여기에 LinearInterpolator 적용
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewModel.capsulePreviewDialogEvent(CapsulePreviewDialogViewModel.CapsulePreviewDialogEvent.MoveCapsuleDetail)
                    viewModel.setVisibleOpenProgressBar(false)
                }
            })
        }
        animator.start()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val capsuleState = Bundle().apply {
            putLong("capsuleId", capsuleId.toLong())
            putBoolean("isOpened", viewModel.capsuleOpenState.value)
        }
        setFragmentResult("capsuleState", capsuleState)
    }

    companion object {
        fun newInstance(capsuleId: String, capsuleType: String, calledFromCamera : Boolean): CapsulePreviewDialogFragment {
            val args = Bundle().apply {
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