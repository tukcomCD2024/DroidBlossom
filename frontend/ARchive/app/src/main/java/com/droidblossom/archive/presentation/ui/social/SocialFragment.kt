package com.droidblossom.archive.presentation.ui.social

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSocialBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.social.adapter.SocialVPA
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment : BaseFragment<SocialViewModelImpl, FragmentSocialBinding>(R.layout.fragment_social) {

    override val viewModel: SocialViewModelImpl by viewModels<SocialViewModelImpl>()

    private val socialVPA by lazy {
        SocialVPA(this)
    }
    override fun observeData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutParams = binding.viewHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.viewHeaderTitle.layoutParams = layoutParams

        initView()
    }

    private fun initView(){
        with(binding){
            socialViewPager.adapter = socialVPA

            TabLayoutMediator(socialTabLayout, socialViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.group)
                    1 -> getString(R.string.friend)
                    else -> null
                }
            }.attach()
        }
    }

    companion object{

        const val TAG = "Social"
        private var reload = true

        fun getReload() = reload
        fun setReloadFalse() {
            reload = false
        }
        fun setReloadTrue() {
            reload = true
        }
        fun newIntent() : SocialFragment{
            return SocialFragment()
        }
    }

}