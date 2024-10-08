package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.domain.model.treasure.response.TreasureCapsuleOpen
import com.droidblossom.archive.domain.model.treasure.response.TreasureCapsuleSummary
import com.droidblossom.archive.util.RetrofitResult

interface TreasureRepository {

    suspend fun openTreasureCapsule(
        capsuleId: Long
    ): RetrofitResult<TreasureCapsuleOpen>

    suspend fun getTreasureCapsuleSummary(
        capsuleId: Long
    ): RetrofitResult<TreasureCapsuleSummary>
}