package com.droidblossom.archive.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
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