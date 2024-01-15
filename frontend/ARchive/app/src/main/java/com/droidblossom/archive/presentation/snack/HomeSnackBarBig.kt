package com.droidblossom.archive.presentation.snack

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.SnackbarHomeAlertWithImageBinding
import com.google.android.material.snackbar.Snackbar

class HomeSnackBarBig(private val view : View, private val message: String, private val imgUrl : String) {

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 5000)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    private val inflater = LayoutInflater.from(context)
    private val snackBarBinding: SnackbarHomeAlertWithImageBinding = DataBindingUtil.inflate(inflater, R.layout.snackbar_home_alert_with_image, null, false)


    init {
        initView()
        initData()
    }

    private fun initView() {
        val location = view.findViewById<View>(R.id.place)

        with(snackBarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 70)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
        snackBar.anchorView = location
    }

    private fun initData() {
        snackBarBinding.openBtn.setOnClickListener {
            Toast.makeText(context,"테스트2", Toast.LENGTH_SHORT).show()
        }
    }

    fun show() {
        snackBar.show()
    }

}