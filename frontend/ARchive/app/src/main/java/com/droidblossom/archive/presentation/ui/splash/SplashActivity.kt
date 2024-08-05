package com.droidblossom.archive.presentation.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivitySplashBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.util.DataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashViewModelImpl, ActivitySplashBinding>(R.layout.activity_splash) {

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    override val viewModel: SplashViewModelImpl by viewModels()
    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.splashEvents.collect{ event ->
                    when(event){
                        is SplashViewModel.SplashEvent.ShowToastMessage->{
                            showToastMessage(event.message)
                        }
                        is SplashViewModel.SplashEvent.Navigation -> {
                            lifecycleScope.launch{
                                if (dataStoreUtils.fetchAccessToken().isNotEmpty() && dataStoreUtils.fetchRefreshToken()
                                        .isNotEmpty()
                                ) {
                                    essentialPermissionLauncher.launch(essentialPermissionList)
                                } else {
                                    AuthActivity.goAuth(this@SplashActivity)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(1000)
            viewModel.getServerCheck()
        }
    }

    private val essentialPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {
                    MainActivity.goMain(this@SplashActivity)
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
                {
                    showToastMessage("ARchive 앱을 사용하려면 카메라, 위치 권한은 필수입니다.")
                    finish()
                },
                {
                    navigateToAppSettings {
                        essentialPermissionLauncher.launch(
                            essentialPermissionList
                        )
                    }
                }
            )
        }
    }

}