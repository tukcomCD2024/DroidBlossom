package com.droidblossom.archive.domain.model.treasure.response

data class TreasureCapsuleOpen (
    val treasureOpenStatus: TreasureOpenStatus,
    val treasureImageUrl: String
)

enum class TreasureOpenStatus(val description: String){
    SUCCESS("SUCCESS"),
    DUPLICATE("DUPLICATE");

    companion object {
        fun fromDescription(description: String): TreasureOpenStatus {
            return values().find { it.description == description }
                ?: throw IllegalArgumentException("Unknown treasureOpenStatus: $description")
        }
    }
}