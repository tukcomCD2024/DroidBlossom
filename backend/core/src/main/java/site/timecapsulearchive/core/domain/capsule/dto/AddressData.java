package site.timecapsulearchive.core.domain.capsule.dto;

import lombok.Builder;

@Builder
public record AddressData(
    String fullRoadAddressName,
    String province,
    String city,
    String subDistinct,
    String roadName,
    String mainBuildingNumber,
    String subBuildingNumber,
    String buildingName,
    String zipCode
) {

}
