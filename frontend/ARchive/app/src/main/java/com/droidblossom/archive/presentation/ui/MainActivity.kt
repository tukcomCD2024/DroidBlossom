package com.droidblossom.archive.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendFragment
import com.droidblossom.archive.presentation.ui.social.page.group.SocialGroupFragment
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.MyFirebaseMessagingService
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModelImpl, ActivityMainBinding>(R.layout.activity_main) {

    @Inject
    lateinit var fcmTokenUseCase: FcmTokenUseCase

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    override val viewModel: MainViewModelImpl by viewModels()

    private var isPermissionRequested = false


    private val arPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val arPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {
                    viewModel.setMainTab(MainPage.AR)
                    //showFragment(CameraFragment.newIntent(), CameraFragment.TAG)
                    //binding.bottomNavigation.selectedItemId = R.id.menuCamera
                }

                permissions.none { it.value } -> {
                    handleAllPermissionsDenied()
                }

                else -> {
                    handlePartialPermissionsDenied(permissions)
                }
            }
        }

    private val essentialPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {

                }

                else -> {
                    handleEssentialPermissionsDenied()
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
                        navigateToAppSettings { arPermissionLauncher.launch(arPermissionList) }
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

    private fun handleEssentialPermissionsDenied() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            showToastMessage("ARchive 앱을 사용하려면 카메라, 위치 권한은 필수입니다.")
        } else {
            showSettingsDialog(PermissionDialogFragment.PermissionType.ESSENTIAL,
                object : PermissionDialogButtonClickListener {
                    override fun onLeftButtonClicked() {
                        showToastMessage("ARchive 앱을 사용하려면 카메라, 위치 권한은 필수입니다.")
                        finish()
                    }

                    override fun onRightButtonClicked() {
                        isPermissionRequested = false
                        navigateToAppSettings {
                            if (essentialPermissionList.any {
                                    ActivityCompat.checkSelfPermission(
                                        this@MainActivity,
                                        it
                                    ) != PackageManager.PERMISSION_GRANTED
                                }) {
                                showToastMessage("ARchive 앱을 사용하려면 카메라, 위치 권한은 필수입니다.")
                                goMain(this@MainActivity)
                            }
                        }
                    }

                })
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
                    navigateToAppSettings { arPermissionLauncher.launch(arPermissionList) }
                }

            })
        }

    }


    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedMainTab
                    .filterNotNull()
                    .collect { tab ->
                        dataStoreUtils.saveSelectedTab(tab.name)
                        Log.d("알림", tab.name)
                        when (tab) {
                            MainPage.HOME -> {
                                showFragment(HomeFragment.newIntent(), HomeFragment.TAG)
                                if (binding.bottomNavigation.selectedItemId != R.id.menuHome){
                                    binding.bottomNavigation.selectedItemId = R.id.menuHome
                                }
                            }

                            MainPage.SKIN -> {
                                showFragment(SkinFragment.newIntent(), SkinFragment.TAG)
                                if (binding.bottomNavigation.selectedItemId != R.id.menuSkin){
                                    binding.bottomNavigation.selectedItemId = R.id.menuSkin
                                }
                            }

                            MainPage.AR -> {
                                showFragment(CameraFragment.newIntent(), CameraFragment.TAG)
                                if (binding.bottomNavigation.selectedItemId != R.id.menuCamera){
                                    binding.bottomNavigation.selectedItemId = R.id.menuCamera
                                }
                            }

                            MainPage.SOCIAL -> {
                                showFragment(SocialFragment.newIntent(), SocialFragment.TAG)
                                if (binding.bottomNavigation.selectedItemId != R.id.menuSocial){
                                    binding.bottomNavigation.selectedItemId = R.id.menuSocial
                                }
                            }

                            MainPage.MY_PAGE -> {
                                showFragment(MyPageFragment.newIntent(), MyPageFragment.TAG)
                                if (binding.bottomNavigation.selectedItemId != R.id.menuMyPage){
                                    binding.bottomNavigation.selectedItemId = R.id.menuMyPage
                                }
                            }

                            else -> {}
                        }
                    }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainEvents.collect { event ->
                    when (event) {
                        MainViewModel.MainEvent.NavigateToHome -> {
                            viewModel.setMainTab(MainPage.HOME)
                        }

                        MainViewModel.MainEvent.NavigateToSkin -> {
                            viewModel.setMainTab(MainPage.SKIN)
                        }

                        MainViewModel.MainEvent.NavigateToCamera -> {
                            arPermissionLauncher.launch(arPermissionList)
                        }

                        MainViewModel.MainEvent.NavigateToSocial -> {
                            viewModel.setMainTab(MainPage.SOCIAL)
                        }

                        MainViewModel.MainEvent.NavigateToMyPage -> {
                            viewModel.setMainTab(MainPage.MY_PAGE)
                        }

                        is MainViewModel.MainEvent.ShowToastMessage -> {

                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //showFragment(HomeFragment.newIntent(), HomeFragment.TAG)
        Log.d("흠", "온 크리에이트 ${viewModel.selectedMainTab.value}")

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
            viewModel.mainEvent(MainViewModel.MainEvent.NavigateToCamera)
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            val currentItemId = binding.bottomNavigation.selectedItemId
            val selectedItemId = menuItem.itemId
            if (currentItemId == selectedItemId) {
                // 같음 - 스크롤 추가
                Log.d("생명", "같음")
                when (selectedItemId) {
                    R.id.menuHome -> {
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuSkin -> {
                        (supportFragmentManager.findFragmentByTag(SkinFragment.TAG) as? SkinFragment)?.scrollToTop()
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuSocial -> {
                        val socialFragment = supportFragmentManager.findFragmentByTag(SocialFragment.TAG) as? SocialFragment
                        socialFragment?.scrollToTopCurrentFragment()
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuMyPage -> {
                        (supportFragmentManager.findFragmentByTag(MyPageFragment.TAG) as? MyPageFragment)?.scrollToTop()
                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        return@setOnItemSelectedListener false
                    }
                }
            } else {
                // 같음 - 스크롤 x
                Log.d("생명", "다름")
                when (selectedItemId) {
                    R.id.menuHome -> {
                        viewModel.mainEvent(MainViewModel.MainEvent.NavigateToHome)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuSkin -> {
                        viewModel.mainEvent(MainViewModel.MainEvent.NavigateToSkin)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuSocial -> {
                        viewModel.mainEvent(MainViewModel.MainEvent.NavigateToSocial)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuMyPage -> {
                        viewModel.mainEvent(MainViewModel.MainEvent.NavigateToMyPage)
                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        return@setOnItemSelectedListener false
                    }
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
            MyFirebaseMessagingService.FragmentDestination.HOME_FRAGMENT.name -> {
                viewModel.setMainTab(MainPage.HOME)
            }

            MyFirebaseMessagingService.FragmentDestination.SKIN_FRAGMENT.name -> {
                viewModel.setMainTab(MainPage.SKIN)
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

    override fun onResume() {
        super.onResume()
        if (!isPermissionRequested) {
            isPermissionRequested = true
            essentialPermissionLauncher.launch(essentialPermissionList)
        } else {
            if (essentialPermissionList.any {
                    ActivityCompat.checkSelfPermission(
                        this,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                }) {
                isPermissionRequested = false
            }
        }
    }


    companion object {
        fun goMain(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(
                "fragmentDestination",
                MyFirebaseMessagingService.FragmentDestination.HOME_FRAGMENT.name
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    enum class MainPage {
        HOME,
        SKIN,
        AR,
        SOCIAL,
        MY_PAGE,
    }
}