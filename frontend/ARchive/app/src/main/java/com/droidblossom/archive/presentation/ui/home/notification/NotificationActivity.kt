package com.droidblossom.archive.presentation.ui.home.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityNotificationBinding
import com.droidblossom.archive.domain.model.member.NotiCategoryName
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.home.notification.adapter.NotificationRVA
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendActivity.Companion.FRIEND
import com.droidblossom.archive.presentation.ui.mypage.friendaccept.FriendAcceptActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity :
    BaseActivity<NotificationViewModelImpl, ActivityNotificationBinding>(R.layout.activity_notification) {

    override val viewModel: NotificationViewModelImpl by viewModels()

    private val notificationRVA by lazy {
        NotificationRVA {
            when (it.categoryName) {
                NotiCategoryName.CAPSULE_SKIN -> {

                }

                NotiCategoryName.FRIEND_REQUEST,
                NotiCategoryName.GROUP_REQUEST -> {
                    startActivity(FriendAcceptActivity.newIntent(this))
                }

                NotiCategoryName.FRIEND_ACCEPT -> {
                    startActivity(FriendActivity.newIntent(this, FriendActivity.FRIEND))
                }

                NotiCategoryName.GROUP_ACCEPT -> {
                    startActivity(FriendActivity.newIntent(this,FriendActivity.GROUP))
                }

                else ->{}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initView()
        viewModel.getNotificationPage()
    }

    private fun initView() {
        val layoutParams = binding.backBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.backBtn.layoutParams = layoutParams

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.rv.adapter = notificationRVA
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalItemViewCount = recyclerView.adapter!!.itemCount - 1

                if (newState == 2 && !recyclerView.canScrollVertically(1)
                    && lastVisibleItemPosition == totalItemViewCount
                ) {
                    viewModel.getNotificationPage()
                }
            }
        })
    }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notifications.collect { notifications ->
                    notificationRVA.submitList(notifications)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationEvent.collect { event ->
                    when (event) {
                        is NotificationViewModel.NotificationEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    companion object {
        const val NOTIFICATION = "notificaiton"

        fun newIntent(context: Context) =
            Intent(context, NotificationActivity::class.java)
    }
}
