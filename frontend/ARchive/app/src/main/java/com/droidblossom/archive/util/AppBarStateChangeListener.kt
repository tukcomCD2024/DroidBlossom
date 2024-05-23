package com.droidblossom.archive.util

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangeListener: AppBarLayout.OnOffsetChangedListener {
    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var currentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout?.let {
            when {
                verticalOffset == 0 -> {
                    if (currentState != State.EXPANDED) {
                        onStateChanged(appBarLayout, State.EXPANDED)
                        currentState = State.EXPANDED
                    }
                }
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    if (currentState != State.COLLAPSED) {
                        onStateChanged(appBarLayout, State.COLLAPSED)
                        currentState = State.COLLAPSED
                    }
                }
                else -> {
                    if (currentState != State.IDLE) {
                        onStateChanged(appBarLayout, State.IDLE)
                        currentState = State.IDLE
                    }
                }
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}