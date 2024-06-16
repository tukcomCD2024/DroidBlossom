package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.treasure.response.TreasureCapsuleSummaryDto
import com.droidblossom.archive.data.source.remote.api.TreasureService
import com.droidblossom.archive.domain.model.treasure.response.TreasureCapsuleSummary
import com.droidblossom.archive.domain.repository.TreasureRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class TreasureRepositoryImpl @Inject constructor(
    private val api: TreasureService
) : TreasureRepository {
    override suspend fun openTreasureCapsule(capsuleId: Long): RetrofitResult<String> {
        return apiHandler({ api.postTreasureCapsuleOpenApi(capsuleId) }) {response: ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun getTreasureCapsuleSummary(capsuleId: Long): RetrofitResult<TreasureCapsuleSummary> {
        return apiHandler({ api.getTreasureCapsuleSummaryApi(capsuleId) }) {response: ResponseBody<TreasureCapsuleSummaryDto> -> response.result.toModel()}
    }
}