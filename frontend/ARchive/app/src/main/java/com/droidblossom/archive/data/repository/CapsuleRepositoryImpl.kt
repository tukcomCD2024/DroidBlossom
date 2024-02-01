package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule.response.NearbyCapsuleResponseDto
import com.droidblossom.archive.data.source.remote.api.CapsuleService
import com.droidblossom.archive.domain.model.capsule.NearbyCapsule
import com.droidblossom.archive.domain.repository.CapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class CapsuleRepositoryImpl @Inject constructor(
    private val api: CapsuleService
) : CapsuleRepository {

    override suspend fun NearbyCapsules(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsule_type: String
    ): RetrofitResult<NearbyCapsule> {
        return apiHandler({ api.getNearbyCapsulesApi(latitude = latitude, longitude = longitude, distance= distance, capsule_type= capsule_type) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toModel() }
    }
}