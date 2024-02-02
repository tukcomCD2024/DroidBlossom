package com.droidblossom.archive.domain.model.common

data class AddressData(
    val buildingName: String?,
    val city: String?,
    val fullRoadAddressName: String,
    val mainBuildingNumber: String?,
    val province: String?,
    val roadName: String?,
    val subBuildingNumber: String?,
    val subDistinct: String?,
    val zipCode: String?
)
