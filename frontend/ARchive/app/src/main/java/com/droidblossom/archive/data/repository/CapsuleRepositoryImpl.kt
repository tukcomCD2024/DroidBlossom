package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleImagesDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleOpenedResponseDto
import com.droidblossom.archive.data.dto.capsule.response.NearbyCapsuleResponseDto
import com.droidblossom.archive.data.source.remote.api.CapsuleService
import com.droidblossom.archive.domain.model.capsule.CapsuleImages
import com.droidblossom.archive.domain.model.capsule.CapsuleOpenedResponse
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchors
import com.droidblossom.archive.domain.model.capsule.CapsuleMarkers
import com.droidblossom.archive.domain.model.common.AddressData
import com.droidblossom.archive.domain.repository.CapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class CapsuleRepositoryImpl @Inject constructor(
    private val api: CapsuleService
) : CapsuleRepository {
    override suspend fun openCapsule(capsuleId: Long): RetrofitResult<CapsuleOpenedResponse> {
        return apiHandler({ api.patchCapsuleOpenApi(capsuleId = capsuleId) }) { response: ResponseBody<CapsuleOpenedResponseDto> -> response.result.toModel() }
    }

    override suspend fun nearbyMyCapsulesHome(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<CapsuleMarkers> {
        return apiHandler({ api.getNearbyMyCapsulesHomeApi(latitude = latitude, longitude = longitude, distance= distance, capsuleType= capsuleType) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toMarkerModel() }
    }

    override suspend fun nearbyMyCapsulesAR(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<CapsuleAnchors> {
        return apiHandler({ api.getNearbyMyCapsulesARApi(latitude = latitude, longitude = longitude, distance= distance, capsuleType= capsuleType) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toAnchorModel() }
    }

    override suspend fun nearbyFriendsCapsulesAR(
        latitude: Double,
        longitude: Double,
        distance: Double,
    ): RetrofitResult<CapsuleAnchors> {
        return apiHandler({ api.getNearbyFriendsCapsulesARApi(latitude = latitude, longitude = longitude, distance= distance) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toAnchorModel() }
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