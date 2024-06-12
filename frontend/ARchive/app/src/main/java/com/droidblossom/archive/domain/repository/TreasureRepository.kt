package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.util.RetrofitResult

interface TreasureRepository {

    suspend fun openTreasureCapsule(
        capsuleId: Long
    ): RetrofitResult<String>
}