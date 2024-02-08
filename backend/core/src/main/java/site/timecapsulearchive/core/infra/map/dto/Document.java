package site.timecapsulearchive.core.infra.map.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Document(

    @JsonProperty(value = "road_address")
    RoadAddressDto roadAddressDto,

    @JsonProperty(value = "address")
    AddressDto addressDto
) {

}
