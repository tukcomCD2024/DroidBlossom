package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityAddFriendBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.util.ContactsUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendActivity : BaseActivity<AddFriendViewModelImpl, ActivityAddFriendBinding>(R.layout.activity_add_friend) {

    override val viewModel: AddFriendViewModelImpl by viewModels()

    override fun observeData() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.contactsSearch(ContactsUtils.getContacts(this))
        } else {
            showToastMessage("권한이 필요합니다.")
        }
    }

    companion object {
        const val ADDFRIEND = "add_friend"

        fun newIntent(context: Context) =
            Intent(context, AddFriendActivity::class.java)
    }
}