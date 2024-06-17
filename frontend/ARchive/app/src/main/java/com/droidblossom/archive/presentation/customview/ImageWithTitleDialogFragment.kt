package com.droidblossom.archive.presentation.customview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentImageWithTitleDialogBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import com.droidblossom.archive.presentation.ui.capsule.ImagesActivity
import com.droidblossom.archive.util.setUrlImg

class ImageWithTitleDialogFragment(
    private val onClick: () -> Unit
) : BaseDialogFragment<FragmentImageWithTitleDialogBinding>(R.layout.fragment_image_with_title_dialog) {

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

        binding.titleTextView.text =  arguments?.getString(DIALOG_TITLE) ?: ""
        binding.imageCV.setOnClickListener{
            onClick()
        }

        arguments?.getString(DIALOG_IMAGE)?.let { ImgUrl ->
            Glide.with(view.context)
                .load(ImgUrl)
                .placeholder(R.drawable.app_symbol)
                .error(R.drawable.app_symbol)
                .apply(RequestOptions().fitCenter())
                .into(binding.postImg)

            binding.imageCV.setOnClickListener{
                startActivity(
                    ImagesActivity.newIntent(
                        requireContext(),
                        arrayOf(ImgUrl),
                        0
                    )
                )
            }
        }

    }

    companion object {

        private const val DIALOG_TITLE = "title"
        private const val DIALOG_IMAGE = "imgUrl"

        fun newIntent(
            title: String,
            imgUrl: String,
            onClick: () -> Unit
        ): ImageWithTitleDialogFragment {
            val args = Bundle().apply {
                putString(DIALOG_TITLE, title)
                putString(DIALOG_IMAGE, imgUrl)
            }
            return ImageWithTitleDialogFragment(onClick = onClick).apply {
                arguments = args
            }
        }
    }
}
