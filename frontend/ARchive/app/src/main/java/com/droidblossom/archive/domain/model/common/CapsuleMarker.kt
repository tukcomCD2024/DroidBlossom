package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.presentation.ui.home.HomeFragment

data class CapsuleMarker(
    val id : Long,
    val longitude : Double,
    val latitude : Double,
    val capsuleType : HomeFragment.CapsuleType
)
