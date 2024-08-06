package com.droidblossom.archive.presentation.customview

import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.DialogExampleImageBinding
import com.droidblossom.archive.databinding.DialogSystemMaintenanceBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment

class SystemMaintenanceDialog(

): BaseDialogFragment<DialogSystemMaintenanceBinding>(R.layout.dialog_system_maintenance) {

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.setCancelable(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            val dialogText =
                "현재 더 나은 서비스 제공을 위해 <b><font color='#FF5C5C'>점검</font></b> 중입니다.<br>이로 인해 사이트 접속이 어려울 수 있습니다.<br>여러분의 양해와 협조에 감사드립니다."
            maintenanceText.text = Html.fromHtml(dialogText, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    companion object {

        const val TAG = "SYSTEM_MAINTENANCE_DIALOG"
        fun newInstance(): SystemMaintenanceDialog = SystemMaintenanceDialog()
    }
}