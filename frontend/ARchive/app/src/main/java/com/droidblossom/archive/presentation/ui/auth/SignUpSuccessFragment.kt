package com.droidblossom.archive.presentation.ui.auth

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSignUpSuccessBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.PermissionDialogButtonClickListener
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpSuccessFragment : BaseFragment<AuthViewModelImpl, FragmentSignUpSuccessBinding>(R.layout.fragment_sign_up_success) {

    override val viewModel : AuthViewModelImpl by activityViewModels()

    override fun observeData() {}

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        // 나중에 가입 후에는 각 권한이 어디에 쓰이는지 알려주는 액티비티로 가서 권한 확인 받을 예정
        essentialPermissionLauncher.launch(essentialPermissionList)
    }

    private val essentialPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {
                    MainActivity.goMain(requireContext())
                }

                else -> {
                    handleEssentialPermissionsDenied()
                }
            }

        }

    private fun handleEssentialPermissionsDenied() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            showToastMessage("ARchive 앱을 사용하려면 카메라, 위치 권한은 필수입니다.")
        } else {
            showSettingsDialog(
                PermissionDialogFragment.PermissionType.ESSENTIAL,
                object : PermissionDialogButtonClickListener {
                    override fun onLeftButtonClicked() {
                        showToastMessage("ARchive 앱을 사용하려면 카메라, 위치 권한은 필수입니다.")
                        requireActivity().finish()
                    }

                    override fun onRightButtonClicked() {
                        navigateToAppSettings{essentialPermissionLauncher.launch(essentialPermissionList)}
                    }

                })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler.postDelayed(runnable, 2000)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}