package com.droidblossom.archive.presentation.ui.mypage.setting.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSettingNoticeBinding
import com.droidblossom.archive.domain.model.setting.Notice
import com.droidblossom.archive.domain.model.setting.NoticeContent
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.mypage.setting.SettingViewModelImpl
import com.droidblossom.archive.presentation.ui.mypage.setting.adapter.NoticeRVA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingNoticeFragment :
    BaseFragment<SettingViewModelImpl, FragmentSettingNoticeBinding>(R.layout.fragment_setting_notice) {

    override val viewModel: SettingViewModelImpl by viewModels()
    lateinit var navController: NavController

    private val noticeAdapter by lazy {
        NoticeRVA()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {
        binding.adapter.adapter = noticeAdapter
        noticeAdapter.submitList(
            listOf(
                Notice(
                    "아카이브 업데이트 : 1.1.0",
                    "2024/03/01",
                    listOf(
                        NoticeContent("업데이트"),
                        NoticeContent("했습니다 : 1.1.0")
                    )
                ),
                Notice(
                    "아카이브 업데이트 : 1.1.0",
                    "2024/03/01",
                    listOf(
                        NoticeContent("업데이트"),
                        NoticeContent("했습니다 : 1.1.0")
                    )
                )
            )
        )
        binding.backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun observeData() {

    }
}