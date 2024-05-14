package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import java.io.Serializable

data class FriendReqStatusResponseDto (
    val httpStatus : String,
    val result : String
) : Serializable {

    fun toModel() = FriendReqStatusResponse(
        httpStatus = this.httpStatus,
        result = this.result,
    )
}