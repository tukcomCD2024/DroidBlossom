package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleImagesDto
import com.droidblossom.archive.data.dto.capsule.response.NearbyCapsuleResponseDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.source.remote.api.CapsuleService
import com.droidblossom.archive.domain.model.capsule.CapsuleImages
import com.droidblossom.archive.domain.model.capsule.NearbyCapsule
import com.droidblossom.archive.domain.model.common.AddressData
import com.droidblossom.archive.domain.repository.CapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class CapsuleRepositoryImpl @Inject constructor(
    private val api: CapsuleService
) : CapsuleRepository {
    override suspend fun openCapsule(capsuleId: Long): RetrofitResult<String> {
        return apiHandler({ api.patchCapsuleOpen(capsuleId = capsuleId) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun NearbyCapsules(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsule_type: String
    ): RetrofitResult<NearbyCapsule> {
        return apiHandler({ api.getNearbyCapsulesApi(latitude = latitude, longitude = longitude, distance= distance, capsule_type= capsule_type) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toModel() }
    }

    override suspend fun getAddress(
        latitude: Double,
        longitude: Double
    ): RetrofitResult<AddressData> {
        return apiHandler({
            api.getAddressApi(
                latitude = latitude,
                longitude = longitude
            )
        }) { response: ResponseBody<AddressDataDto> -> response.result.toModel() }
    }

    override suspend fun getCapsuleImages(
        size: Int,
        capsuleId: Int
    ): RetrofitResult<CapsuleImages> {
        return apiHandler({
            api.getCapsuleImagesApi(
                size = size,
                capsuleId = capsuleId
            )
        }) { response: ResponseBody<CapsuleImagesDto> -> response.result.toModel() }
    }
}