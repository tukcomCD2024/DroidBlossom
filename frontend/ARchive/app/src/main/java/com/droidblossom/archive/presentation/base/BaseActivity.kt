package com.droidblossom.archive.presentation.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.droidblossom.archive.presentation.customview.LoadingDialog
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.model.AppEvent
import com.droidblossom.archive.presentation.ui.NetworkConnectionActivity
import com.droidblossom.archive.util.ClipboardUtil
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseActivity<VM: BaseViewModel?, V: ViewDataBinding>(@LayoutRes val layoutResource :Int): AppCompatActivity() {

    abstract val viewModel: VM?
    private var fetchJob: Job? = null

    private var _binding: V? = null
    protected val binding: V get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private var loadingState = false

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var onSettingsResult: () -> Unit

    val essentialPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    abstract fun observeData()

    protected fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    fun onEvent(event: AppEvent) {
        when (event) {
            is AppEvent.NetworkDisconnectedEvent -> {
                startActivity(NetworkConnectionActivity.newIntent(this))
            }
            is AppEvent.NotificationReceivedEvent -> {
                //showToastMessage("알림")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        fetchJob = viewModel?.fetchData()
        observeData()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onSettingsResult()
        }

        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
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

    override fun onDestroy() {
        fetchJob?.let {
            if (it.isActive) {
                it.cancel()
            }
            dismissLoading()
        }
        super.onDestroy()
    }

    fun copyText(label:String, text: String) {
        ClipboardUtil.copyTextToClipboard(this, label, text)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
            showToastMessage("클립보드에 복사되었어요.")
        }
    }
    fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showSettingsDialog(permissionType: PermissionDialogFragment.PermissionType, onNegativeClick: () -> Unit, onPositiveClick: () -> Unit) {
        val existingDialog = supportFragmentManager.findFragmentByTag(PermissionDialogFragment.TAG) as DialogFragment?
        if (existingDialog == null) {
            val dialog = PermissionDialogFragment.newInstance(permissionType.name, onNegativeClick, onPositiveClick)
            dialog.show(supportFragmentManager, PermissionDialogFragment.TAG)
        }
    }
    fun navigateToAppSettings(onComplete: () -> Unit) {
        onSettingsResult = onComplete  // 저장된 콜백 설정
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            val uri = Uri.fromParts("package", packageName, null)
            data = uri
        }
        resultLauncher.launch(intent)  // 설정 화면으로 이동
    }
}