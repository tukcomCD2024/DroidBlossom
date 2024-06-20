package com.droidblossom.archive.presentation.ui.social

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSocialBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.skin.SkinFragment
import com.droidblossom.archive.presentation.ui.social.adapter.SocialVPA
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendFragment
import com.droidblossom.archive.presentation.ui.social.page.group.SocialGroupFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment : BaseFragment<SocialViewModelImpl, FragmentSocialBinding>(R.layout.fragment_social) {

    var currentPage = 0

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

            socialViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                    Log.d("생명", "다름")
                }
            })

            TabLayoutMediator(socialTabLayout, socialViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.group)
                    1 -> getString(R.string.friend)
                    else -> null
                }
            }.attach()

        }
    }

    fun scrollToTopCurrentFragment() {
        when (val fragment = socialVPA.getFragment(currentPage)) {
            is SocialGroupFragment -> fragment.scrollToTop()
            is SocialFriendFragment -> fragment.scrollToTop()
        }
    }
    companion object{

        const val TAG = "SOCIAL"
        private var reload = true
        fun getReload() = reload
        fun setReloadFalse() {
            reload = false
        }
        fun setReloadTrue() {
            reload = true
        }
        fun newIntent() : SocialFragment = SocialFragment()
    }


}