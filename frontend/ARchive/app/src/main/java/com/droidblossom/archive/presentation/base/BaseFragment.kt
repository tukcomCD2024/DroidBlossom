package com.droidblossom.archive.presentation.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.droidblossom.archive.presentation.customview.LoadingDialog
import com.droidblossom.archive.presentation.customview.PermissionDialogButtonClickListener
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.util.ClipboardUtil
import kotlinx.coroutines.Job

abstract class BaseFragment<VM: BaseViewModel, V: ViewDataBinding>(@LayoutRes val layoutResource :Int): Fragment() {

    abstract val viewModel: VM
    private lateinit var fetchJob: Job

    private var _binding: V? = null
    protected val binding : V get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private var loadingState = false

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    val essentialPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    protected fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

    private lateinit var onSettingsResult: () -> Unit
    abstract fun observeData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            layoutInflater,
            layoutResource,
            null,
            false
        )
        //DataBiding Observe
        binding.lifecycleOwner =this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchJob = viewModel.fetchData()
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onSettingsResult()
        }
        observeData()
    }

    fun copyText(label:String, text: String) {
        ClipboardUtil.copyTextToClipboard(requireContext(), label, text)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
            showToastMessage("클립보드에 복사되었어요.")
        }
    }

    fun showToastMessage(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showSettingsDialog(permissionType: PermissionDialogFragment.PermissionType, listener: PermissionDialogButtonClickListener) {

        val existingDialog = parentFragmentManager.findFragmentByTag(PermissionDialogFragment.TAG) as DialogFragment?

        if (existingDialog == null) {
            val dialog = PermissionDialogFragment.newInstance(permissionType.name, listener)
            dialog.show(parentFragmentManager, PermissionDialogFragment.TAG)
        }

    }

    fun navigateToAppSettings(onComplete: () -> Unit) {
        onSettingsResult = onComplete  // 저장된 콜백 설정
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            data = uri
        }
        resultLauncher.launch(intent)  // 설정 화면으로 이동
    }

    fun showLoading(context: Context) {
        if (!loadingState) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            loadingState = true
        }
    }

    fun dismissLoading() {
        if (loadingState) {
            loadingDialog.dismiss()
            loadingState = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        dismissLoading()
        _binding = null
    }
}