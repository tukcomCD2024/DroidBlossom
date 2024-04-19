package com.droidblossom.archive.presentation.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.droidblossom.archive.presentation.customview.HomeSnackBarSmall
import com.droidblossom.archive.presentation.customview.LoadingDialog
import com.droidblossom.archive.util.ClipboardUtil
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity<VM: BaseViewModel?, V: ViewDataBinding>(@LayoutRes val layoutResource :Int): AppCompatActivity() {

    abstract val viewModel: VM?
    private var fetchJob: Job? = null

    private var _binding: V? = null
    protected val binding: V get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private var loadingState = false

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printId(event: Map<String, String>) {
        // 스낵바 호출로 바꾸면 될듯?
        Log.d("이베", "$event")
        binding.root.let { rootView ->
            //HomeSnackBarSmall(rootView).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        fetchJob = viewModel?.fetchData()
        observeData()
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
}