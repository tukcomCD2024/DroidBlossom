package com.droidblossom.archive.presentation.customview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.DialogExampleImageBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment

class ExampleImageDialog(

): BaseDialogFragment<DialogExampleImageBinding>(R.layout.dialog_example_image) {

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.setCancelable(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        const val TAG = "EXAMPLE_IMAGE_DIALOG"
        fun newInstance(): ExampleImageDialog = ExampleImageDialog()
    }
}