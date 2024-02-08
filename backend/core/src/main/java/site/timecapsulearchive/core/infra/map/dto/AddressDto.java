package site.timecapsulearchive.core.infra.map.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AddressDto(
    String addressName,
    String region1depthName,
    String region2depthName,
    String region3depthName,
    String roadName,
    String mainBuildingNumber,
    String subBuildingNumber,
    String buildingName,
    String zoneNo
) {

}
