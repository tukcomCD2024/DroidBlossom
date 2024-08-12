package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentFriendSearchNumberBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter.AddFriendRVA
import com.droidblossom.archive.util.ContactsUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFriendNumberFragment :
    BaseFragment<AddFriendViewModelImpl, FragmentFriendSearchNumberBinding>(R.layout.fragment_friend_search_number) {

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
                    viewModel.contactsSearch(ContactsUtils.getContacts(requireContext()))
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
                showSettingsDialog(PermissionDialogFragment.PermissionType.CONTACTS_AND_CALL,
                    {
                        showToastMessage("앱에서 친구를 찾기 위해 연락처, 전화 접근 권한이 필요합니다.")
                        requireActivity().finish()
                    },
                    {
                        navigateToAppSettings {
                            requestContactsCallPermissionLauncher.launch(
                                permissionsToRequest
                            )
                        }
                    }
                )
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                showToastMessage("앱에서 친구를 찾기 위해 연락처 접근 권한이 필요합니다.")
            } else {
                showSettingsDialog(
                    PermissionDialogFragment.PermissionType.CONTACTS,
                    {
                        showToastMessage("앱에서 친구를 찾기 위해 연락처 접근 권한이 필요합니다.")
                        requireActivity().finish()
                    },
                    {
                        navigateToAppSettings {
                            requestContactsCallPermissionLauncher.launch(
                                permissionsToRequest
                            )
                        }
                    }
                )
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
            showSettingsDialog(permissionType,
                {
                    showToastMessage("앱에서 친구를 찾기 위해 ${permissionType.description} 권한이 필요합니다.")
                    requireActivity().finish()
                },
                {
                    navigateToAppSettings {
                        requestContactsCallPermissionLauncher.launch(
                            permissionsToRequest
                        )
                    }
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        navController = Navigation.findNavController(view)
        initView()

        requestContactsCallPermissionLauncher.launch(permissionsToRequest)
    }

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    private fun initView() {

        val layoutParams = binding.closeBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.closeBtn.layoutParams = layoutParams

        binding.recycleView.adapter = addFriendRVA
        binding.recycleView.setHasFixedSize(true)

        binding.closeBtn.setOnClickListener {
            navController.popBackStack()
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

        binding.searchOpenBtnT.setOnClickListener {
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            viewModel.searchTag()
        }

        binding.searchOpenEditT.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.closeSearchNum()
            }
        }
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = binding.searchOpenBtn
                val outRect = Rect()
                focusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    focusedView.clearFocus()
                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
            false
        }

        binding.searchOpenEditT.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    viewModel.searchFriend()
                }
            }
        })

        binding.recycleView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = binding.searchOpenBtn
                val outRect = Rect()
                focusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    val imm = requireActivity().getSystemService(InputMethodManager::class.java)
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
            false
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

                        is AddFriendViewModel.AddEvent.CloseLoading -> {
                            dismissLoading()
                        }

                        is AddFriendViewModel.AddEvent.OpenLoading -> {
                            showLoading(requireContext())
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