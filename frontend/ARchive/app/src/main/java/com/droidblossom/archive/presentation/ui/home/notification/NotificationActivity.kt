package com.droidblossom.archive.presentation.ui.home.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityNotificationBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity :
    BaseFragment<NotificationViewModelImpl, ActivityNotificationBinding>(R.layout.activity_notification) {

    override val viewModel: NotificationViewModelImpl by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }

    override fun observeData() {

    }
}
