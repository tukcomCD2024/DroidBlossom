package com.droidblossom.archive.util

import androidx.fragment.app.FragmentManager

interface FragmentManagerProvider {
    fun provideFragmentManager(): FragmentManager
}