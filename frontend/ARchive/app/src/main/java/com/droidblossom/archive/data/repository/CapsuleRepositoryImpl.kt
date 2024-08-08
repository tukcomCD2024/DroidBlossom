package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleImagesDto
import com.droidblossom.archive.data.dto.capsule.response.CapsuleOpenedResponseDto
import com.droidblossom.archive.data.dto.capsule.response.NearbyCapsuleResponseDto
import com.droidblossom.archive.data.dto.common.toModel
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
    private val capsuleService: CapsuleService
) : CapsuleRepository {
    override suspend fun openCapsule(capsuleId: Long): RetrofitResult<CapsuleOpenedResponse> {
        return apiHandler({ capsuleService.patchCapsuleOpenApi(capsuleId = capsuleId) }) { response: ResponseBody<CapsuleOpenedResponseDto> -> response.result.toModel() }
    }

    override suspend fun nearbyMyCapsulesHome(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<CapsuleMarkers> {
        return apiHandler({ capsuleService.getNearbyMyCapsulesHomeApi(latitude = latitude, longitude = longitude, distance= distance, capsuleType= capsuleType) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toMarkerModel() }
    }

    override suspend fun nearbyFriendsCapsulesHome(
        latitude: Double,
        longitude: Double,
        distance: Double
    ): RetrofitResult<CapsuleMarkers> {
        return apiHandler({ capsuleService.getNearbyFriendsCapsulesHomeApi(latitude = latitude, longitude = longitude, distance= distance ) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toMarkerModel() }

    }

    override suspend fun nearbyMyCapsulesAR(
        latitude: Double,
        longitude: Double,
        distance: Double,
        capsuleType: String
    ): RetrofitResult<CapsuleAnchors> {
        return apiHandler({ capsuleService.getNearbyMyCapsulesARApi(latitude = latitude, longitude = longitude, distance= distance, capsuleType= capsuleType) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toAnchorModel() }
    }

    override suspend fun nearbyFriendsCapsulesAR(
        latitude: Double,
        longitude: Double,
        distance: Double,
    ): RetrofitResult<CapsuleAnchors> {
        return apiHandler({ capsuleService.getNearbyFriendsCapsulesARApi(latitude = latitude, longitude = longitude, distance= distance) }) { response : ResponseBody<NearbyCapsuleResponseDto> -> response.result.toAnchorModel() }
    }

    override suspend fun getAddress(
        latitude: Double,
        longitude: Double
    ): RetrofitResult<AddressData> {
        return apiHandler({
            capsuleService.getAddressApi(
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
            capsuleService.getCapsuleImagesApi(
                size = size,
                capsuleId = capsuleId
            )
        }) { response: ResponseBody<CapsuleImagesDto> -> response.result.toModel() }
    }

    override suspend fun deleteCapsule(
        capsuleId: Long,
        capsuleType: String
    ): RetrofitResult<String> {
        return apiHandler({
            capsuleService.deleteCapsuleApi(
                capsuleId = capsuleId,
                capsuleType = capsuleType
            )
        }) { response: ResponseBody<String> -> response.result.toString() }
    }

    override suspend fun reportCapsule(capsuleId: Long): RetrofitResult<String> {
        return apiHandler({ capsuleService.patchCapsuleDeclarationApi(capsuleId = capsuleId) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

}