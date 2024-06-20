package site.timecapsulearchive.core.common.dependency;

import org.opengis.referencing.FactoryException;
import site.timecapsulearchive.core.global.geography.GeoTransformConfig;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashProperties;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.config.S3Properties;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

/**
 * 유닛 테스트를 위한 의존성 클래스 집합체
 */
public class UnitTestDependency {

    public static S3PreSignedUrlManager s3PreSignedUrlManager() {
        S3Config s3Config = new S3Config(
            new S3Properties("a".repeat(32), "b".repeat(32), "origin", "temporary", "us-east"));
        return new S3PreSignedUrlManager(s3Config);
    }

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
}
