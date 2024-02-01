package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.domain.model.capsule.NearbyCapsule
import com.droidblossom.archive.util.RetrofitResult

interface CapsuleRepository {

    suspend fun NearbyCapsules(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<NearbyCapsule>
}