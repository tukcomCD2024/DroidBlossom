package com.droidblossom.archive.presentation.ui.mypage.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityFriendBinding
import com.droidblossom.archive.databinding.ActivitySettingBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendActivity :
    BaseActivity<FriendViewModelImpl, ActivityFriendBinding>(R.layout.activity_friend) {
    override val viewModel: FriendViewModelImpl by viewModels<FriendViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView(){
        
    }

    override fun observeData() {
    }
}