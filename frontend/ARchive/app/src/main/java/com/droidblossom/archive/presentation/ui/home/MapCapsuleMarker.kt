package com.droidblossom.archive.presentation.ui.home

import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.naver.maps.geometry.LatLng
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

data class MapCapsuleMarker(
    val capsuleMarker: CapsuleMarker
) : TedClusterItem {

    override fun getTedLatLng(): TedLatLng = TedLatLng(capsuleMarker.latitude,capsuleMarker.longitude)


}