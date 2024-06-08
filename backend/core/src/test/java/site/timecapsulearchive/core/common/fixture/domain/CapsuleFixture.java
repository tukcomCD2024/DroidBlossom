package site.timecapsulearchive.core.common.fixture.domain;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule.CapsuleBuilder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.common.supplier.ZonedDateTimeSupplier;
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
            .dueDate(ZonedDateTimeSupplier.utc().get())
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
     * 그룹 캡슐의 테스트 픽스처를 생성한다
     *
     * @param member      그룹 캡슐을 생성할 멤버
     * @param capsuleSkin 캡슐 스킨
     * @param group       그룹
     * @return 그룹 캡슐 테스트 픽스처
     */
    public static Capsule groupCapsule(Member member, CapsuleSkin capsuleSkin, Group group) {
        return Capsule.builder()
            .dueDate(ZonedDateTimeSupplier.utc().get())
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

    private static void setFieldValue(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static CapsuleBuilder defaultGroupCapsuleBuilder(Member member, CapsuleSkin capsuleSkin,
        Group group) {
        return Capsule.builder()
            .title("testTitle")
            .content("testContent")
            .type(CapsuleType.GROUP)
            .address(getTestAddress())
            .point(geoTransformManager.changePoint4326To3857(TEST_LATITUDE, TEST_LONGITUDE))
            .member(member)
            .capsuleSkin(capsuleSkin)
            .group(group);
    }

    public static Optional<Capsule> groupCapsuleSpecificTime(
        Long memberId,
        Long capsuleId,
        ZonedDateTime zonedDateTime
    ) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(zonedDateTime)
            .build();

        setFieldValue(capsule, "id", capsuleId);

        return Optional.ofNullable(capsule);
    }

    private static CapsuleBuilder getCapsuleBuilder(Long memberId) {
        Member member = MemberFixture.memberWithMemberId(memberId);
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        Group group = GroupFixture.group();

        CapsuleBuilder capsuleBuilder = defaultGroupCapsuleBuilder(member, capsuleSkin, group);
        return capsuleBuilder;
    }

    public static Optional<Capsule> groupCapsuleNotAllMemberOpen(
        Long memberId,
        Long capsuleId,
        List<Member> groupMembers
    ) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(ZonedDateTimeSupplier.utc().get())
            .build();

        List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(false,
            capsule, groupMembers);
        setFieldValue(capsule, "id", capsuleId);
        setFieldValue(capsule, "groupCapsuleOpens", groupCapsuleOpens);

        return Optional.ofNullable(capsule);
    }

    public static Optional<Capsule> groupCapsuleHalfMemberOpen(
        Long memberId,
        Long capsuleId,
        List<Member> groupMembers
    ) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(ZonedDateTimeSupplier.utc().get())
            .build();

        List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpensNotAllOpened(
            capsule, groupMembers);
        setFieldValue(capsule, "id", capsuleId);
        setFieldValue(capsule, "groupCapsuleOpens", groupCapsuleOpens);
        return Optional.ofNullable(capsule);
    }

    public static Optional<Capsule> groupCapsuleEmptyOpen(
        Long memberId,
        Long capsuleId
    ) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(ZonedDateTimeSupplier.utc().get())
            .build();

        setFieldValue(capsule, "id", capsuleId);

        return Optional.ofNullable(capsule);
    }

    public static Optional<Capsule> groupCapsuleAllMemberOpen(
        Long memberId,
        Long capsuleId,
        List<Member> groupMembers
    ) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(ZonedDateTimeSupplier.utc().get())
            .build();

        List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(true,
            capsule, groupMembers);
        setFieldValue(capsule, "id", capsuleId);
        setFieldValue(capsule, "groupCapsuleOpens", groupCapsuleOpens);

        return Optional.ofNullable(capsule);
    }

    public static Optional<Capsule> groupCapsuleExcludeSpecificMember(
        Long memberId,
        Long capsuleId,
        List<Member> groupMembers
    ) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(ZonedDateTimeSupplier.utc().get())
            .build();

        List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpensNotOpenSpecificMemberId(
            capsule, groupMembers, memberId);
        setFieldValue(capsule, "id", capsuleId);
        setFieldValue(capsule, "groupCapsuleOpens", groupCapsuleOpens);

        return Optional.ofNullable(capsule);
    }

    public static Optional<Capsule> groupCapsuleAlreadyOpen(Long memberId, Long capsuleId) {
        CapsuleBuilder capsuleBuilder = getCapsuleBuilder(memberId);
        Capsule capsule = capsuleBuilder.dueDate(ZonedDateTimeSupplier.utc().get())
            .build();

        setFieldValue(capsule, "id", capsuleId);
        setFieldValue(capsule, "isOpened", true);

        return Optional.ofNullable(capsule);
    }

    public static List<Capsule> groupCapsules(
        Member owner,
        CapsuleSkin capsuleSkin,
        Group group,
        int maxCount
    ) {
        List<Capsule> result = new ArrayList<>();
        for (int count = 0; count < maxCount; count++) {
            result.add(groupCapsule(owner, capsuleSkin, group));
        }

        return result;
    }
}
