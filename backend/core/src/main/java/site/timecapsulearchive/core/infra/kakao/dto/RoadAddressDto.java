package site.timecapsulearchive.core.infra.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record RoadAddressDto(
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