package site.timecapsulearchive.core.common.dependency;

import org.opengis.referencing.FactoryException;
import site.timecapsulearchive.core.global.config.security.JwtProperties;
import site.timecapsulearchive.core.global.geography.GeoTransformConfig;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.AESKeyProperties;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashProperties;
import site.timecapsulearchive.core.global.security.jwt.JwtFactory;
import site.timecapsulearchive.core.infra.sms.data.response.SmsApiResponse;
import site.timecapsulearchive.core.infra.sms.manager.SmsApiManager;

/**
 * 유닛 테스트를 위한 의존성 클래스 집합체
 */
public class UnitTestDependency {

    public static GeoTransformManager geoTransformManager() {
        GeoTransformConfig geoTransformConfig = new GeoTransformConfig();

        try {
            return new GeoTransformManager(geoTransformConfig.geometryFactoryOf4326(),
                geoTransformConfig.geometryFactoryOf3857(),
                geoTransformConfig.mathTransform4326To3857(),
                geoTransformConfig.mathTransform3857To4326()
            );
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashEncryptionManager hashEncryptionManager() {
        return new HashEncryptionManager(new HashProperties("test"));
    }

    public static JwtProperties jwtProperties() {
        return new JwtProperties("T".repeat(32), 86400000L, 2592000000L, 3600000L);
    }

    public static JwtFactory jwtFactory() {
        return new JwtFactory(jwtProperties());
    }

    public static AESEncryptionManager aesEncryptionManager() {
        return new AESEncryptionManager(new AESKeyProperties("A".repeat(32)));
    }

    public static SmsApiManager smsApiManager() {
        return (receiver, message) -> new SmsApiResponse(200, "success");
    }
}
