package com.droidblossom.archive.presentation.customview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.DialogPermissionBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment

class PermissionDialogFragment(
    private val onClick: () -> Unit
) : BaseDialogFragment<DialogPermissionBinding>(R.layout.dialog_permission) {

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

        val permissionType = arguments?.getString("permission")

        when(permissionType){
            PermissionType.LOCATION.name -> {
                binding.messageT.text = "ARchive 은 위치 권한이 필수입니다. 앱을 사용하려면 위치 권한을 허용해 주세요."
                binding.permissionImg.setImageResource(R.drawable.ic_location_outline)
                binding.leftBtn.text = "앱 종료"
            }
            PermissionType.CAMERA.name -> {
                binding.messageT.text = "AR 기능을 사용하려면 카메라 권한이 필요합니다. '권한'에서 카메라 권한을 허용해 주세요."
                binding.permissionImg.setImageResource(R.drawable.ic_camera_outline)
            }
            PermissionType.CONTACTS.name -> {
                binding.messageT.text = "연락처를 통해 앱 내에서 친구를 찾아보세요. 권한을 허용하면 친구 찾기가 가능합니다."
                binding.permissionImg.setImageResource(R.drawable.ic_concats_outline)
            }
            PermissionType.NOTIFICATIONS.name -> {
                binding.messageT.text = "ARchive 앱의 알림을 받기 위해서는 알림 권한이 필요합니다. 알림을 통해 중요한 정보와 업데이트를 놓치지 마세요."
                binding.permissionImg.setImageResource(R.drawable.ic_alarm_24)
            }
        }


        binding.leftBtn.setOnClickListener {
            this.dismiss()
        }

        binding.rightBtn.setOnClickListener {
            onClick()
            this.dismiss()
        }
    }

    companion object {

        fun newIntent(
            permission: String,
            onRightClick: () -> Unit,
        ): PermissionDialogFragment {
            val args = Bundle().apply {
                putString("permission", permission)
            }
            return PermissionDialogFragment(onRightClick).apply {
                arguments = args
            }
        }
    }

    enum class PermissionType(val description: String){
        LOCATION("위치"),
        CAMERA("카메라"),
        CONTACTS("연락처"),
        NOTIFICATIONS("알람")
    }
}