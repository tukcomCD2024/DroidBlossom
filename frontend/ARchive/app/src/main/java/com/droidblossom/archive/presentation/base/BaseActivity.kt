package com.droidblossom.archive.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel?, V: ViewDataBinding>(@LayoutRes val layoutResource :Int): AppCompatActivity() {

    abstract val viewModel: VM?
    private var fetchJob: Job? = null

    private var _binding: V? = null
    protected val binding: V get() = _binding!!

    abstract fun observeData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        fetchJob = viewModel?.fetchData()
        observeData()
    }

    override fun onDestroy() {
        fetchJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        super.onDestroy()
    }

    fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}