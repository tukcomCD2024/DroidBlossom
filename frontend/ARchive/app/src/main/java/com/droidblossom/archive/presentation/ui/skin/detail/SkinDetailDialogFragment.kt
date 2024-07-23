package com.droidblossom.archive.presentation.ui.skin.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinDetailDialogBinding
import com.droidblossom.archive.databinding.PopupMenuCapsuleBinding
import com.droidblossom.archive.databinding.PopupMenuSkinBinding
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkinDetailDialogFragment :
    BaseDialogFragment<FragmentSkinDetailDialogBinding>(R.layout.fragment_skin_detail_dialog){

    private val viewModel: SkinDetailViewModelImpl by viewModels()

    private val skin: CapsuleSkinSummary? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("skin", CapsuleSkinSummary::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("skin")
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.CENTER)

            val params = dialog.window?.attributes
            dialog.window?.attributes = params
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        skin?.let {
            viewModel.setSkin(it)
        }

        initView()
    }

    private fun initView(){
        with(binding){
            menuImg.setOnClickListener { view ->
                showPopupMenu(view)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenuBinding = PopupMenuSkinBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        val density = requireContext().resources.displayMetrics.density
        val widthPixels = (120 * density).toInt()

        val popupWindow = PopupWindow(
            popupMenuBinding.root,
            widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupMenuBinding.menuDelete.setOnClickListener {
            viewModel.deleteSkin()
            popupWindow.dismiss()
        }

        view.post {

            popupWindow.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = popupWindow.contentView.measuredWidth
            //val popupHeight = popupWindow.contentView.measuredHeight

            val xOff = -(popupWidth + view.width)
            val yOff = -view.height

            popupWindow.showAsDropDown(view, xOff, yOff)

        }
    }
    companion object {

        const val TAG = "SkinDetail"

        fun newInstance(skin: CapsuleSkinSummary): SkinDetailDialogFragment {
            val args = Bundle().apply {
                putParcelable("skin", skin)
            }
            return SkinDetailDialogFragment().apply {
                arguments = args
            }
        }
    }
}