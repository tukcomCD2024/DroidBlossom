package site.timecapsulearchive.core.infra.map.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.Address;

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

    public Address toAddress() {
        return Address.builder()
            .fullRoadAddressName(fullRoadAddressName)
            .province(province)
            .city(city)
            .subDistinct(subDistinct)
            .roadName(roadName)
            .mainBuildingNumber(mainBuildingNumber)
            .subBuildingNumber(subBuildingNumber)
            .buildingName(buildingName)
            .zipCode(zipCode)
            .build();
    }

}
