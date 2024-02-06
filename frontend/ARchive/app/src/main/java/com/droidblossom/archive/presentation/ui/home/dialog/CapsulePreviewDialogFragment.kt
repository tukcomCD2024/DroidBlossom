package com.droidblossom.archive.presentation.ui.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCapsulePreviewDialogBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.presentation.ui.capsule.CapsuleDetailActivity
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import com.droidblossom.archive.util.CapsuleTypeUtils
import dagger.hilt.android.AndroidEntryPoint
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

        initObserver()
        initView()
    }

    private fun initView() {
        with(binding) {
            skinCardView.setOnClickListener {
                if (viewModel.secretCapsuleSummary.value.isOpened) {
                    val intent = CapsuleDetailActivity.newIntent(requireContext(), capsuleId.toLong(), capsuleType!!)
                    startActivity(intent)
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
}