package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityGroupDetailBinding
import com.droidblossom.archive.databinding.PopupMenuCapsuleBinding
import com.droidblossom.archive.databinding.PopupMenuGroupBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend.FriendDetailActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend.FriendDetailViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.adapter.GroupDetailVPA
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.ManagementGroupMemberActivity
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page.GroupCapsuleFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.page.GroupMemberFragment
import com.droidblossom.archive.util.AppBarStateChangeListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupDetailActivity :
    BaseActivity<GroupDetailViewModelImpl, ActivityGroupDetailBinding>(R.layout.activity_group_detail) {

    override val viewModel: GroupDetailViewModelImpl by viewModels<GroupDetailViewModelImpl>()

    var currentPage = 0

    private val groupId: Long by lazy {
        intent.getLongExtra(GROUP_ID, -1)
    }

    private val groupVPA by lazy {
        GroupDetailVPA(this)
    }

    override fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupDetailEvents.collect { event ->
                    when(event){
                        is GroupDetailViewModel.GroupDetailEvent.ShowToastMessage -> {

                        }
                        GroupDetailViewModel.GroupDetailEvent.SwipeRefreshLayoutDismissLoading -> {
                            if (binding.swipeRefreshLayout.isRefreshing){
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }

                        GroupDetailViewModel.GroupDetailEvent.LeaveGroupSuccess -> {
                            showToastMessage("그룹에서 나왔습니다. 함께해 주셔서 고마워요!")
                            finish()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupInfo.collect {
                    binding.tabLayout.getTabAt(GROUP_CAPSULE)?.text = getString(R.string.groupCapsule) + "(${viewModel.groupInfo.value.groupCapsuleNum})"

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupMembers.collect {
                    binding.tabLayout.getTabAt(GROUP_MEMBER)?.text = getString(R.string.groupMember) + "(${viewModel.groupMembers.value.size + 1}/30)"

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAppBarExpanded.collect {
                    binding.swipeRefreshLayout.isEnabled = it
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        val layoutParams = binding.appBarLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.appBarLayout.layoutParams = layoutParams
        initView()
        initTab()
        viewModel.setGroupId(groupId)
    }

    private fun initView(){
        with(binding){
            setSupportActionBar(groupDetailToolbar)
            swipeRefreshLayout.setOnChildScrollUpCallback { _, _ ->
                val fragment = groupVPA.getFragment(vp.currentItem)
                when (fragment) {
                    is GroupCapsuleFragment -> {
                        val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupCapsuleRV)
                        recyclerView?.let { recyclerView.canScrollVertically(-1) } ?: true
                    }
                    is GroupMemberFragment -> {
                        val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.groupMemberRV)
                        recyclerView?.let { recyclerView.canScrollVertically(-1) } ?: true
                    }
                    else -> true
                }
            }

            binding.swipeRefreshLayout.setOnRefreshListener {
                if (viewModel.isAppBarExpanded.value){
                    viewModel.getGroupDetail()
                }
            }

            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                }
            })

            appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
                override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                    when (state) {
                        State.EXPANDED -> {
                            viewModel.setIsAppBarExpanded(true)
                        }
                        State.COLLAPSED -> {
                            viewModel.setIsAppBarExpanded(false)
                        }
                        State.IDLE -> {
                            viewModel.setIsAppBarExpanded(false)
                        }
                    }
                }

            })

        }
    }


    private fun initTab(){
        with(binding){

            vp.adapter = groupVPA

            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                }
            })

            TabLayoutMediator(tabLayout, vp) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.groupCapsule)
                    1 -> getString(R.string.groupMember)
                    else -> null
                }
            }.attach()

            tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val fragment = groupVPA.getFragment(currentPage)
                    when (fragment) {
                        is GroupCapsuleFragment -> {
                            fragment.view?.findViewById<RecyclerView>(R.id.groupCapsuleRV)?.scrollToPosition(0)
                        }
                        is GroupMemberFragment -> {
                            fragment.view?.findViewById<RecyclerView>(R.id.groupMemberRV)?.scrollToPosition(0)
                        }
                    }
                }

            })


        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenuBinding = PopupMenuGroupBinding.inflate(LayoutInflater.from(this@GroupDetailActivity), null, false)

        val density = this.resources.displayMetrics.density
        val widthPixels = (120 * density).toInt()
        val xOffset = (16 * density).toInt()
        val yOffset = (getStatusBarHeight() * density).toInt()/2

        val popupWindow = PopupWindow(
            popupMenuBinding.root,
            widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        if (viewModel.groupInfo.value.hasEditPermission){
            popupMenuBinding.menuGroupClosure.visibility = View.VISIBLE
            popupMenuBinding.menuGroupMemberManagement.visibility = View.VISIBLE
        }else{
            popupMenuBinding.menuGroupClosure.visibility = View.GONE
            popupMenuBinding.menuGroupMemberManagement.visibility = View.GONE
        }

        popupMenuBinding.menuMap.setOnClickListener {
            popupWindow.dismiss()
        }
        popupMenuBinding.menuGroupClosure.setOnClickListener {
            popupWindow.dismiss()
        }
        popupMenuBinding.menuGroupMemberManagement.setOnClickListener {
            startActivity(ManagementGroupMemberActivity.newIntent(this, viewModel.groupId.value))
            popupWindow.dismiss()
        }

        view.post {

            popupWindow.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = popupWindow.contentView.measuredWidth
            //val popupHeight = popupWindow.contentView.measuredHeight

            popupWindow.showAtLocation(view, Gravity.END or Gravity.TOP, xOffset, yOffset)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("메뉴","엥")
        return when(item.itemId){
            R.id.toolBarMenu -> {
                val toolbar: View = findViewById(R.id.groupDetailToolbar)
                showPopupMenu(toolbar)
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }

    companion object {
        private const val GROUP_ID = "group_id"
        private const val GROUP_CAPSULE = 0
        private const val GROUP_MEMBER = 1
        fun newIntent(context: Context, groupId: Long) =
            Intent(context, GroupDetailActivity::class.java).apply {
                putExtra(GROUP_ID, groupId)
            }
    }
}