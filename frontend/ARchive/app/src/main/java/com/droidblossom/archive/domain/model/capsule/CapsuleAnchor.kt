package com.droidblossom.archive.domain.model.capsule

import com.droidblossom.archive.presentation.ui.home.HomeFragment

data class CapsuleAnchor(
    val id : Long,
    val longitude : Double,
    val latitude : Double,
    val capsuleType : HomeFragment.CapsuleType,
    val skinUrl : String
)
