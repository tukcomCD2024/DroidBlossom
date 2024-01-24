package site.timecapsulearchive.core.domain.capsule.dto.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.infra.kakao.dto.AddressDto;
import site.timecapsulearchive.core.infra.kakao.dto.RoadAddressDto;

@Component
public class AddressMapper {

    public Address roadAddressToAddress(RoadAddressDto dto) {
        return Address.builder()
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

    public Address addressDtoToAddress(AddressDto dto) {
        return Address.builder()
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
