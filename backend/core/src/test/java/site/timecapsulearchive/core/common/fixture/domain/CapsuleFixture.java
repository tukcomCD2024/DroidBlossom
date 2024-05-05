package site.timecapsulearchive.core.common.fixture.domain;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

public class CapsuleFixture {

    private static final GeoTransformManager geoTransformManager = UnitTestDependency.geoTransformManager();
    private static final double TEST_LATITUDE = 37.078;
    private static final double TEST_LONGITUDE = 127.423;

    public static List<Capsule> capsules(int size, Member member, CapsuleSkin capsuleSkin,
        CapsuleType capsuleType) {
        return IntStream
            .range(0, size)
            .mapToObj(i -> capsule(member, capsuleSkin, capsuleType))
            .toList();
    }

    public static Capsule capsule(Member member, CapsuleSkin capsuleSkin, CapsuleType capsuleType) {
        return Capsule.builder()
            .dueDate(ZonedDateTime.now())
            .title("testTitle")
            .content("testContent")
            .type(capsuleType)
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

    /**
     * 그룹 캡슐의 테스트 픽스처들을 생성한다
     *
     * @param size 테스트 픽스처를 만들 캡슐의 개수
     * @param member      그룹 캡슐을 생성할 멤버
     * @param capsuleSkin 캡슐 스킨
     * @param group       그룹
     * @return 그룹 캡슐 테스트 픽스처
     */
    public static List<Capsule> groupCapsules(int size, Member member, CapsuleSkin capsuleSkin, Group group) {
        return IntStream
            .range(0, size)
            .mapToObj(i -> groupCapsule(member, capsuleSkin, group))
            .toList();
    }

    /**
     * 그룹 캡슐의 테스트 픽스처를 생성한다
     *
     * @param member 그룹 캡슐을 생성할 멤버
     * @param capsuleSkin 캡슐 스킨
     * @param group 그룹
     * @return 그룹 캡슐 테스트 픽스처
     */
    public static Capsule groupCapsule(Member member, CapsuleSkin capsuleSkin, Group group) {
        return Capsule.builder()
            .dueDate(ZonedDateTime.now())
            .title("testTitle")
            .content("testContent")
            .type(CapsuleType.GROUP)
            .address(getTestAddress())
            .point(geoTransformManager.changePoint4326To3857(TEST_LATITUDE, TEST_LONGITUDE))
            .member(member)
            .capsuleSkin(capsuleSkin)
            .group(group)
            .build();
    }
}
