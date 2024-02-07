package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto

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
){
    constructor() :this(null,null,"",null,null,null,null,null,null)
    fun toDto()=AddressDataDto(
        buildingName = this.buildingName ?: "",
        city = this.city ?: "",
        fullRoadAddressName = this.fullRoadAddressName,
        mainBuildingNumber = this.mainBuildingNumber ?: "",
        province = this.mainBuildingNumber ?: "",
        roadName = this.roadName ?:"",
        subBuildingNumber = this.subBuildingNumber ?:"",
        subDistinct = this.subDistinct ?:"",
        zipCode = this.zipCode ?:""
    )
}
