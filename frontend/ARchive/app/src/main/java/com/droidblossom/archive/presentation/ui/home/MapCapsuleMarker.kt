package com.droidblossom.archive.presentation.ui.home

import com.droidblossom.archive.domain.model.capsule.CapsuleMarker
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

data class MapCapsuleMarker(
    val capsuleAnchor: CapsuleMarker
) : TedClusterItem {

    override fun getTedLatLng(): TedLatLng = TedLatLng(capsuleAnchor.latitude,capsuleAnchor.longitude)


}