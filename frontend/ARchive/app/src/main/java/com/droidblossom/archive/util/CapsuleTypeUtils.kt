package com.droidblossom.archive.util

import com.droidblossom.archive.presentation.ui.home.HomeFragment

object CapsuleTypeUtils {

    fun enumToString(capsuleType: HomeFragment.CapsuleType): String {
        return capsuleType.name
    }

    fun stringToEnum(capsuleTypeString: String): HomeFragment.CapsuleType {
        return HomeFragment.CapsuleType.valueOf(capsuleTypeString)
    }
}