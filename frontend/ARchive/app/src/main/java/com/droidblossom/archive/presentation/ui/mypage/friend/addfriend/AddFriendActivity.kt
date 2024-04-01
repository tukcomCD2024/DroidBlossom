package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityAddFriendBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendActivity : BaseActivity<AddFriendViewModelImpl, ActivityAddFriendBinding>(R.layout.activity_add_friend) {

    override val viewModel: AddFriendViewModelImpl by viewModels()

    override fun observeData() {}

    companion object {
        const val ADDFRIEND = "add_friend"

        fun newIntent(context: Context) =
            Intent(context, AddFriendActivity::class.java)
    }
}