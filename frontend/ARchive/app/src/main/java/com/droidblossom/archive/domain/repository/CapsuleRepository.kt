package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.domain.model.capsule.CapsuleImages
import com.droidblossom.archive.domain.model.capsule.NearbyCapsule
import com.droidblossom.archive.domain.model.common.AddressData
import com.droidblossom.archive.util.RetrofitResult

interface CapsuleRepository {

    suspend fun openCapsule(
        capsuleId : Long
    ): RetrofitResult<String>

    suspend fun NearbyCapsules(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<NearbyCapsule>

    suspend fun getAddress(
        latitude: Double,
        longitude: Double,
    ):RetrofitResult<AddressData>

    suspend fun getCapsuleImages(
        size : Int,
        capsuleId : Int,
    ): RetrofitResult<CapsuleImages>
}