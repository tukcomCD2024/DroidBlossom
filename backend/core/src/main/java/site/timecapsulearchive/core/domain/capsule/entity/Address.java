package site.timecapsulearchive.core.domain.capsule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "full_road_address_name")
    private String fullRoadAddressName;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "sub_district")
    private String subDistinct;

    @Column(name = "road_name")
    private String roadName;

    @Column(name = "main_building_number")
    private String mainBuildingNumber;

    @Column(name = "sub_building_number")
    private String subBuildingNumber;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "zip_code")
    private String zipCode;

    private Address(String fullRoadAddressName) {
        this.fullRoadAddressName = fullRoadAddressName;
    }

    @Builder
    private Address(
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
        this.fullRoadAddressName = fullRoadAddressName;
        this.province = province;
        this.city = city;
        this.subDistinct = subDistinct;
        this.roadName = roadName;
        this.mainBuildingNumber = mainBuildingNumber;
        this.subBuildingNumber = subBuildingNumber;
        this.buildingName = buildingName;
        this.zipCode = zipCode;
    }

    public static Address from(String fullRoadAddressName) {
        return new Address(fullRoadAddressName);
    }
}
