package site.timecapsulearchive.core.infra.map.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;
import site.timecapsulearchive.core.infra.map.data.dto.AddressDto;
import site.timecapsulearchive.core.infra.map.data.dto.RoadAddressDto;

@Component
public class AddressMapper {

    public AddressData addressDtoToAddress(final AddressDto dto) {
        return AddressData.builder()
            .fullRoadAddressName(dto.addressName())
            .province(dto.region1depthName())
            .city(dto.region2depthName())
            .subDistinct(dto.region3depthName())
            .roadName(dto.roadName())
            .mainBuildingNumber(dto.mainBuildingNumber())
            .subBuildingNumber(dto.subBuildingNumber())
            .buildingName(dto.buildingName())
            .zipCode(dto.zoneNo())
            .build();
    }

    public AddressData roadAddressToData(final RoadAddressDto dto) {
        return AddressData.builder()
            .fullRoadAddressName(dto.addressName())
            .province(dto.region1depthName())
            .city(dto.region2depthName())
            .subDistinct(dto.region3depthName())
            .roadName(dto.roadName())
            .mainBuildingNumber(dto.mainBuildingNumber())
            .subBuildingNumber(dto.subBuildingNumber())
            .buildingName(dto.buildingName())
            .zipCode(dto.zoneNo())
            .build();
    }
}
