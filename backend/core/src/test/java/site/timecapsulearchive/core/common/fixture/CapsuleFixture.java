package site.timecapsulearchive.core.common.fixture;

import org.opengis.referencing.FactoryException;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformConfig;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

public class CapsuleFixture {

    private static final GeoTransformManager geoTransformManager;
    private static final double x = 37.078;
    private static final double y = 127.423;

    static {
        try {
            GeoTransformConfig geoTransformConfig = new GeoTransformConfig();
            geoTransformManager = new GeoTransformManager(
                geoTransformConfig.geometryFactoryOf4326(),
                geoTransformConfig.geometryFactoryOf3857(),
                geoTransformConfig.mathTransform4326To3857(),
                geoTransformConfig.mathTransform3857To4326()
            );
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

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
}
