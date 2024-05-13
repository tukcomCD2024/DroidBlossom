package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentFriendSearchNicknameBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.PermissionDialogButtonClickListener
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.ui.camera.CameraFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter.AddFriendRVA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFriendNicknameFragment :
    BaseFragment<AddFriendViewModelImpl, FragmentFriendSearchNicknameBinding>(R.layout.fragment_friend_search_nickname) {

    override val viewModel: AddFriendViewModelImpl by viewModels()

    lateinit var navController: NavController

    private val addFriendRVA by lazy {
        AddFriendRVA { position ->
            viewModel.checkAddFriendList(position)
        }
    }

    private val permissionsToRequest: Array<String> by lazy {
        val permissionsList = mutableListOf<String>()
        permissionsList.add(Manifest.permission.READ_CONTACTS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissionsList.add(Manifest.permission.READ_PHONE_NUMBERS)
        }

        permissionsList.toTypedArray()
    }

    private val requestContactsCallPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {
                    navController.navigate(R.id.action_searchFriendNicknameFragment_to_searchFriendNumberFragment)
                }
                permissions.none { it.value } -> {
                    handleAllPermissionsDenied()
                }
                else -> {
                    handlePartialPermissionsDenied(permissions)
                }
            }
        }

    private fun handleAllPermissionsDenied() {
        if (permissionsToRequest.size > 1) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) ||
                shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_NUMBERS)
            ) {
                showToastMessage("앱에서 친구를 찾기 위해 연락처, 전화 접근 권한이 필요합니다.")
            } else {
                showSettingsDialog(
                    PermissionDialogFragment.PermissionType.CONTACTS_AND_CALL, object : PermissionDialogButtonClickListener {
                        override fun onLeftButtonClicked() {
                            showToastMessage("앱에서 친구를 찾기 위해 연락처, 전화 접근 권한이 필요합니다.")
                        }

                        override fun onRightButtonClicked() {
                            navigateToAppSettings {
                                requestContactsCallPermissionLauncher.launch(permissionsToRequest)
                            }
                        }
                    }
                )
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                showToastMessage("앱에서 친구를 찾기 위해 연락처 접근 권한이 필요합니다.")
            } else {
                showSettingsDialog(PermissionDialogFragment.PermissionType.CONTACTS, object : PermissionDialogButtonClickListener{
                    override fun onLeftButtonClicked() {
                        showToastMessage("앱에서 친구를 찾기 위해 연락처 접근 권한이 필요합니다.")
                    }

                    override fun onRightButtonClicked() {
                        navigateToAppSettings{
                            requestContactsCallPermissionLauncher.launch(permissionsToRequest)
                        }
                    }
                })
            }
        }
    }

    private fun handlePartialPermissionsDenied(permissions: Map<String, Boolean>) {
        permissions.forEach { (perm, granted) ->
            if (!granted) {
                when (perm) {
                    Manifest.permission.READ_CONTACTS ->
                        showPermissionDialog(PermissionDialogFragment.PermissionType.CONTACTS)

                    Manifest.permission.READ_PHONE_NUMBERS ->
                        showPermissionDialog(PermissionDialogFragment.PermissionType.CALL)
                }
            }
        }
    }

    private fun showPermissionDialog(permissionType: PermissionDialogFragment.PermissionType) {
        if (shouldShowRequestPermissionRationale(permissionType.toString())) {
            showToastMessage("앱에서 친구를 찾기 위해 ${permissionType.description} 권한이 필요합니다.")
        } else {
            showSettingsDialog(permissionType, object : PermissionDialogButtonClickListener {
                override fun onLeftButtonClicked() {
                    showToastMessage("앱에서 친구를 찾기 위해 ${permissionType.description} 권한이 필요합니다.")
                }

                override fun onRightButtonClicked() {
                    navigateToAppSettings {
                        requestContactsCallPermissionLauncher.launch(permissionsToRequest)
                    }
                }

            })
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {

        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.recycleView.adapter = addFriendRVA
        binding.recycleView.setHasFixedSize(true)

        binding.closeBtn.setOnClickListener {
            (activity as AddFriendActivity).finish()
        }

        binding.searchOpenEditT.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchOpenEditT.text.isNullOrEmpty()) {
                    viewModel.searchTag()
                }
                true
            }
            false
        }
        binding.searchOpenEditT.addTextChangedListener {
            viewModel.resetList()
        }

        binding.addCV.setOnClickListener {
            requestContactsCallPermissionLauncher.launch(permissionsToRequest)
        }

        binding.searchOpenBtnT.setOnClickListener {
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            viewModel.searchTag()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addEvent.collect { event ->
                    when (event) {
                        is AddFriendViewModel.AddEvent.ShowToastMessage -> {
                            showToastMessage(event.message)
                        }

                        is AddFriendViewModel.AddEvent.NotificationChange -> {
                            addFriendRVA.notifyDataSetChanged()
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addFriendListUI.collect { friends ->
                    addFriendRVA.submitList(friends)
                }
            }
        }
    }
}