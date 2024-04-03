package site.timecapsulearchive.core.common.fixture;

import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

public class CapsuleFixture {

    private static final GeoTransformManager geoTransformManager = UnitTestDependency.geoTransformManager();
    private static final double x = 37.078;
    private static final double y = 127.423;

    public static Capsule publicCapsule(Member member, CapsuleSkin capsuleSkin) {
        return Capsule.builder()
            .title("test_title")
            .content("test_content")
            .dueDate(null)
            .type(CapsuleType.PUBLIC)
            .point(geoTransformManager.changePoint4326To3857(x, y))
            .member(member)
            .address(Address.from("full_test_address"))
            .capsuleSkin(capsuleSkin)
            .build();
    }

    public static List<Capsule> publicCapsules(Member member, CapsuleSkin capsuleSkin) {
        return IntStream
            .range(0, 10)
            .mapToObj(i -> {
                return Capsule.builder()
                    .title(i + "test_title")
                    .content(i + "test_content")
                    .dueDate(null)
                    .type(CapsuleType.PUBLIC)
                    .point(geoTransformManager.changePoint4326To3857(x, y))
                    .member(member)
                    .address(Address.from("full_test_address"))
                    .capsuleSkin(capsuleSkin)
                    .build();
            })
            .toList();
    }
}
