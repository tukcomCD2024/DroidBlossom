package com.droidblossom.archive.presentation.customview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.DialogPermissionBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment

interface PermissionDialogButtonClickListener {
    fun onLeftButtonClicked()
    fun onRightButtonClicked()
}

class PermissionDialogFragment(
    private val listener: PermissionDialogButtonClickListener,
) : BaseDialogFragment<DialogPermissionBinding>(R.layout.dialog_permission) {

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

        val permissionType = arguments?.getString("permission") ?: ""
        setupPermissionInfo(permissionType)

        binding.leftBtn.text = if (permissionType == PermissionType.ESSENTIAL.name) "앱 종료" else "취소"

        binding.leftBtn.setOnClickListener {
            listener.onLeftButtonClicked()
            this.dismiss()
        }

        binding.rightBtn.setOnClickListener {
            listener.onRightButtonClicked()
            this.dismiss()
        }
    }

    private fun setupPermissionInfo(permissionType: String) {
        val info = when (permissionType) {
            PermissionType.LOCATION.name -> Pair(
                "ARchive 은 위치 권한이 필수입니다. 앱을 사용하려면 위치 권한을 허용해 주세요.",
                R.drawable.ic_location_outline
            )
            PermissionType.CAMERA.name -> Pair(
                "AR 기능을 사용하려면 카메라 권한이 필요합니다. '권한'에서 카메라 권한을 허용해 주세요.",
                R.drawable.ic_camera_outline
            )
            PermissionType.CONTACTS.name -> Pair(
                "연락처를 통해 앱 내에서 친구를 찾아보세요. 연락처와 전화 권한을 허용하면 친구 찾기가 가능합니다.",
                R.drawable.ic_contacts_outline
            )
            PermissionType.NOTIFICATIONS.name -> Pair(
                "ARchive 앱의 알림을 받기 위해서는 알림 권한이 필요합니다. 알림을 통해 중요한 정보와 업데이트를 놓치지 마세요.",
                R.drawable.ic_alarm_24
            )
            PermissionType.CALL.name -> Pair(
                "연락처를 통해 앱 내에서 친구를 찾아보세요. 전화 권한을 허용하면 친구 찾기가 가능합니다.",
                R.drawable.ic_call_24
            )
            PermissionType.CONTACTS_AND_CALL.name -> Pair(
                "연락처를 통해 앱 내에서 친구를 찾아보세요. 전화, 연락처 권한을 허용하면 친구 찾기가 가능합니다.",
                R.drawable.ic_contact_phone_outline
            )
            PermissionType.AR.name -> Pair(
                "AR 기능을 사용하려면 카메라, 위치 권한이 필요합니다. '권한'에서 카메라, 위치 권한을 허용해 주세요.",
                R.drawable.ic_ar_outline
            )
            PermissionType.ESSENTIAL.name -> Pair(
                "ARchive 앱을 사용하려면 카메라, 위치 권한이 필수입니다. '권한'에서 카메라, 위치 권한을 허용해 주세요.",
                R.drawable.ic_essential_outline
            )
            else -> Pair("", R.drawable.ic_default_outline)
        }

        binding.messageT.text = info.first
        if (info.second != 0) {
            binding.permissionImg.setImageResource(info.second)
        }
    }

    companion object {

        const val TAG = "PERMISSION_DIALOG"
        fun newInstance(
            permission: String,
            listener: PermissionDialogButtonClickListener
        ): PermissionDialogFragment {
            val args = Bundle().apply {
                putString("permission", permission)
            }
            return PermissionDialogFragment(listener).apply {
                arguments = args
            }
        }
    }

    enum class PermissionType(val description: String) {
        LOCATION("위치"),
        CAMERA("카메라"),
        CONTACTS("연락처"),
        NOTIFICATIONS("알람"),
        CALL("전화"),
        CONTACTS_AND_CALL("연락처, 전화"),
        AR("카메라, 위치"),
        ESSENTIAL("필수")
    }
}