package site.timecapsulearchive.core.infra.map.data.dto;

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
