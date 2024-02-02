package com.droidblossom.archive.data.dto.capsule.response

import com.droidblossom.archive.domain.model.common.AddressData

data class AddressDataDto(
    val buildingName: String,
    val city: String,
    val fullRoadAddressName: String,
    val mainBuildingNumber: String,
    val province: String,
    val roadName: String,
    val subBuildingNumber: String,
    val subDistinct: String,
    val zipCode: String
){
    fun toModel() = AddressData(
        buildingName = this.buildingName,
        city =  this.city,
        fullRoadAddressName = this.fullRoadAddressName,
        mainBuildingNumber = this.mainBuildingNumber,
        province = this.province,
        roadName = this.roadName,
        subBuildingNumber = this.subBuildingNumber,
        subDistinct = this.subDistinct,
        zipCode = this.zipCode
    )

}