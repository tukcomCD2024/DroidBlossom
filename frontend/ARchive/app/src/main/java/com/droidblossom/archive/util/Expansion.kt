package com.droidblossom.archive.util

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.Serializable

fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(key, clazz)
    } else {
        this.getSerializableExtra(key) as T?
    }
}

fun Activity.getStatusBarHeight(): Int {
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) this.resources.getDimensionPixelSize(resourceId) else 0
}

fun ConstraintLayout.LayoutParams.updateTopConstraintsForSearch(
    isSearchOpen: Boolean,
    searchOpenView: View,
    searchView: View,
    additionalMarginDp: Float,
    resources: Resources
) {
    this.topToBottom = if (isSearchOpen) searchOpenView.id else searchView.id
    val additionalMargin = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, additionalMarginDp, resources.displayMetrics).toInt()
    this.topMargin = additionalMargin
}