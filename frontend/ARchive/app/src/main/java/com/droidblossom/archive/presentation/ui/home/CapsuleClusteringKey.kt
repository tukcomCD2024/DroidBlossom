package com.droidblossom.archive.presentation.ui.home

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

class CapsuleClusteringKey(val id: Long, val capsuleType: HomeFragment.CapsuleType, private val position : LatLng) : ClusteringKey {

    override fun getPosition() = position

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as CapsuleClusteringKey
        return id == itemKey.id
    }

    override fun hashCode() = id.hashCode()

}