package com.droidblossom.archive.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.droidblossom.archive.R
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.databinding.ActivityMainBinding
import com.droidblossom.archive.domain.usecase.member.FcmTokenUseCase
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.customview.PermissionDialogButtonClickListener
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.ui.camera.CameraFragment
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendActivity
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptActivity
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import com.droidblossom.archive.presentation.ui.social.SocialFragment
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.MyFirebaseMessagingService
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<Nothing?, ActivityMainBinding>(R.layout.activity_main) {

    @Inject
    lateinit var fcmTokenUseCase: FcmTokenUseCase

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    override val viewModel: Nothing? = null
    lateinit var viewBinding: ActivityMainBinding

    private val arPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {
                    showFragment(CameraFragment.newIntent(), CameraFragment.TAG)
                    binding.bottomNavigation.selectedItemId = R.id.menuCamera
                }

                permissions.none { it.value } -> {
                    handleAllPermissionsDenied()
                }

                else -> {
                    handlePartialPermissionsDenied(permissions)
                }
            }
        }

    private fun handleAllPermissionsDenied() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            showToastMessage("AR 기능을 사용하려면 카메라, 위치 권한이 필요합니다.")
        } else {
            showSettingsDialog(
                PermissionDialogFragment.PermissionType.AR,
                object : PermissionDialogButtonClickListener {
                    override fun onLeftButtonClicked() {
                        showToastMessage("AR 기능을 사용하려면 카메라, 위치 권한이 필요합니다.")
                    }

                    override fun onRightButtonClicked() {
                        navigateToAppSettings { requestPermissionLauncher.launch(arPermissionList) }
                    }

                })
        }
    }

    private fun handlePartialPermissionsDenied(permissions: Map<String, Boolean>) {
        permissions.forEach { (permission, granted) ->
            if (!granted) {
                when (permission) {
                    Manifest.permission.CAMERA -> showPermissionDialog(PermissionDialogFragment.PermissionType.CAMERA)
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if (!permissions.getValue(Manifest.permission.ACCESS_FINE_LOCATION) ||
                            !permissions.getValue(Manifest.permission.ACCESS_COARSE_LOCATION)
                        ) {
                            showPermissionDialog(PermissionDialogFragment.PermissionType.LOCATION)
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionDialog(permissionType: PermissionDialogFragment.PermissionType) {

        if (shouldShowRequestPermissionRationale(permissionType.toString())) {
            showToastMessage("AR 기능을 사용하려면 ${permissionType.description} 권한이 필요합니다.")
        } else {
            showSettingsDialog(permissionType, object : PermissionDialogButtonClickListener {
                override fun onLeftButtonClicked() {
                    showToastMessage("AR 기능을 사용하려면 ${permissionType.description} 권한이 필요합니다.")
                }

                override fun onRightButtonClicked() {
                    navigateToAppSettings { requestPermissionLauncher.launch(arPermissionList) }
                }

            })
        }

    }


    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = binding
        showFragment(HomeFragment.newIntent(), HomeFragment.TAG)

        initBottomNav()
        initFCM()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            setIntent(it)
            handleIntent(it)
        }
    }

    private fun initFCM() {
        CoroutineScope(Dispatchers.IO).launch {
            fcmTokenUseCase(
                FcmTokenRequsetDto(MyFirebaseMessagingService().getFirebaseToken())
            ).collect { result ->
                result.onSuccess {
                    Log.d("FCM", "fcm patch 성공")
                }.onFail {
                    Log.d("FCM", "fcm patch 실패")
                }
            }
        }
    }

    private fun initBottomNav() {
        binding.fab.setOnClickListener {
            requestPermissionLauncher.launch(arPermissionList)
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    showFragment(HomeFragment.newIntent(), HomeFragment.TAG)
                    return@setOnItemSelectedListener true
                }

                R.id.menuSkin -> {
                    showFragment(SkinFragment.newIntent(), SkinFragment.TAG)
                    return@setOnItemSelectedListener true
                }

                R.id.menuSocial -> {
                    showFragment(SocialFragment(), SocialFragment.TAG)
                    return@setOnItemSelectedListener true
                }

                R.id.menuMyPage -> {
                    showFragment(MyPageFragment.newIntent(), MyPageFragment.TAG)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }

    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainNavHost, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

    // 현재 포그라운드에서만 이동 됨 - 서버와 얘기해야함
    private fun handleIntent(intent: Intent) {
        val destination = intent.getStringExtra("fragmentDestination")
        Log.d("알림", destination.toString())

        when (destination) {
            MyFirebaseMessagingService.FragmentDestination.SKIN_FRAGMENT.name -> {
                showFragment(SkinFragment.newIntent(), SkinFragment.TAG)
            }

            MyFirebaseMessagingService.FragmentDestination.FRIEND_REQUEST_ACTIVITY.name -> {
                startActivity(FriendAcceptActivity.newIntent(this, FriendAcceptActivity.FRIEND))
            }

            MyFirebaseMessagingService.FragmentDestination.FRIEND_ACCEPT_ACTIVITY.name -> {
                startActivity(FriendActivity.newIntent(this, FriendActivity.FRIEND))
            }

            else -> {

            }
        }
    }


    companion object {
        fun goMain(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

}