package site.timecapsulearchive.core.common.data.fixture;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

public class CapsuleFixture {

    private final static double TEST_LATITUDE = 37.287458;
    private final static double TEST_LONGITUDE = 127.014372;
    private static final GeoTransformManager geoTransformManager = UnitTestDependency.geoTransformManager();

    public static Capsule testCapsule(int dataPrefix, Member member, CapsuleSkin capsuleSkin) {
        return Capsule.builder()
            .dueDate(ZonedDateTime.now())
            .title(dataPrefix + "testTitle")
            .content(dataPrefix + "testContent")
            .type(CapsuleType.ALL)
            .address(getTestAddress())
            .point(geoTransformManager.changePoint4326To3857(TEST_LATITUDE, TEST_LONGITUDE))
            .member(member)
            .capsuleSkin(capsuleSkin)
            .build();
    }

    private static Address getTestAddress() {
        return Address.builder()
            .fullRoadAddressName("testFullRoadAddressName")
            .province("testProvince")
            .city("testCity")
            .subDistinct("testSubDistinct")
            .roadName("testRoadName")
            .mainBuildingNumber("testMainBuildingNumber")
            .subBuildingNumber("testSubBuildingNumber")
            .buildingName("testBuildingName")
            .zipCode("testZipCode")
            .build();
    }
}
