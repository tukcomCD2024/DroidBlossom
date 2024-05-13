package com.droidblossom.archive.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class CustomLifecycleOwner : LifecycleOwner {

    private val _customLifecycle = LifecycleRegistry(this)

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        _customLifecycle.handleLifecycleEvent(event)
    }

    override val lifecycle: Lifecycle
        get() = _customLifecycle
}
