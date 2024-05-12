package com.droidblossom.archive.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class CustomLifecycleOwner : LifecycleOwner {

    private val customLifecycle = LifecycleRegistry(this)

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        customLifecycle.handleLifecycleEvent(event)
    }

    override val lifecycle: Lifecycle
        get() = customLifecycle
}
