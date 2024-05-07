package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.domain.model.capsule.CapsuleImages
import com.droidblossom.archive.domain.model.capsule.CapsuleOpenedResponse
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchors
import com.droidblossom.archive.domain.model.capsule.CapsuleMarkers
import com.droidblossom.archive.domain.model.common.AddressData
import com.droidblossom.archive.util.RetrofitResult

interface CapsuleRepository {

    suspend fun openCapsule(
        capsuleId : Long
    ): RetrofitResult<CapsuleOpenedResponse>

    suspend fun nearbyMyCapsulesHome(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<CapsuleMarkers>

    suspend fun nearbyFriendsCapsulesHome(
        latitude: Double,
        longitude: Double,
        distance: Double,
    ): RetrofitResult<CapsuleMarkers>

    suspend fun nearbyMyCapsulesAR(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<CapsuleAnchors>

    suspend fun nearbyFriendsCapsulesAR(
        latitude: Double,
        longitude: Double,
        distance: Double,
    ): RetrofitResult<CapsuleAnchors>

    suspend fun getAddress(
        latitude: Double,
        longitude: Double,
    ):RetrofitResult<AddressData>

    suspend fun getCapsuleImages(
        size : Int,
        capsuleId : Int,
    ): RetrofitResult<CapsuleImages>
}