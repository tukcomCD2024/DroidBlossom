package com.droidblossom.archive.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.droidblossom.archive.R
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.databinding.ActivityMainBinding
import com.droidblossom.archive.domain.usecase.member.FcmTokenUseCase
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.camera.CameraFragment
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import com.droidblossom.archive.presentation.ui.social.SocialFragment
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.MyFirebaseMessagingService
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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

    private fun initFCM(){
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

    private fun initBottomNav(){
        binding.fab.setOnClickListener {
            showFragment(CameraFragment.newIntent(), CameraFragment.TAG)
            binding.bottomNavigation.selectedItemId = R.id.menuCamera
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

        when (destination) {
            MyFirebaseMessagingService.FragmentDestination.SKIN_FRAGMENT.name -> {
                showFragment(SkinFragment.newIntent(), SkinFragment.TAG)
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