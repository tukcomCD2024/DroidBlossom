package com.droidblossom.archive.presentation.ui.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCapsulePreviewDialogBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CapsulePreviewDialogFragment : BaseDialogFragment<FragmentCapsulePreviewDialogBinding>(R.layout.fragment_capsule_preview_dialog) {

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
        val capsuleId = arguments?.getString("capsule_id")
        val capsuleType = arguments?.getString("capsule_type")
        Log.d("다이얼로그", "${capsuleId},${capsuleType}")
    }

    private fun initObserver(){

    }
}