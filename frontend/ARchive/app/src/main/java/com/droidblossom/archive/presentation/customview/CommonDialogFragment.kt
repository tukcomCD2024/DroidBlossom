package com.droidblossom.archive.presentation.customview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCommonDialogBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment

class CommonDialogFragment(
    private val onClick: () -> Unit
) : BaseDialogFragment<FragmentCommonDialogBinding>(R.layout.fragment_common_dialog) {

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messageT.text = arguments?.getString(DIALOG_TITLE) ?: ""
        binding.rightBtn.text = arguments?.getString(DIALOG_RIGHT_BTN_TEXT) ?: "확인"

        binding.leftBtn.setOnClickListener {
            this.dismiss()
        }

        binding.rightBtn.setOnClickListener {
            onClick()
            this.dismiss()
        }
    }

    companion object {

        private const val DIALOG_TITLE = "title"
        private const val DIALOG_RIGHT_BTN_TEXT = "rightBtnT"

        fun newIntent(
            title: String,
            rightBtnT: String,
            onRightClick: () -> Unit
        ): CommonDialogFragment {
            val args = Bundle().apply {
                putString(DIALOG_TITLE, title)
                putString(DIALOG_RIGHT_BTN_TEXT, rightBtnT)
            }
            return CommonDialogFragment(onRightClick).apply {
                arguments = args
            }
        }
    }
}